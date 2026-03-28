package RBPO.proj.service;

import RBPO.proj.dto.AdmitPetWithAppointmentRequest;
import RBPO.proj.dto.AdmitPetWithAppointmentResponse;
import RBPO.proj.dto.CancelAppointmentRequest;
import RBPO.proj.dto.CompleteVisitWithTreatmentRequest;
import RBPO.proj.dto.OwnerSummaryResponse;
import RBPO.proj.dto.VetScheduleRow;
import RBPO.proj.model.Appointment;
import RBPO.proj.model.Pet;
import RBPO.proj.model.Treatment;
import RBPO.proj.repository.AppointmentRepository;
import RBPO.proj.repository.OwnerRepository;
import RBPO.proj.repository.PetRepository;
import RBPO.proj.repository.TreatmentRepository;
import RBPO.proj.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperationService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentRepository treatmentRepository;
    private final AppointmentService appointmentService;

    public OperationService(OwnerRepository ownerRepository,
                            PetRepository petRepository,
                            VetRepository vetRepository,
                            AppointmentRepository appointmentRepository,
                            TreatmentRepository treatmentRepository,
                            AppointmentService appointmentService) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.appointmentRepository = appointmentRepository;
        this.treatmentRepository = treatmentRepository;
        this.appointmentService = appointmentService;
    }

    /**
     * Бизнес-операция: завести питомца у владельца и сразу записать на приём (Pet + Appointment).
     */
    @Transactional
    public AdmitPetWithAppointmentResponse admitPetWithAppointment(AdmitPetWithAppointmentRequest request) {
        if (request.getOwnerId() == null || !ownerRepository.existsById(request.getOwnerId())) {
            throw new IllegalArgumentException("Owner with id " + request.getOwnerId() + " not found");
        }
        if (request.getPetName() == null || request.getPetName().isBlank()) {
            throw new IllegalArgumentException("petName is required");
        }
        if (request.getSpecies() == null || request.getSpecies().isBlank()) {
            throw new IllegalArgumentException("species is required");
        }

        Pet pet = new Pet();
        pet.setName(request.getPetName().trim());
        pet.setSpecies(request.getSpecies().trim());
        pet.setBreed(request.getBreed());
        pet.setOwnerId(request.getOwnerId());
        pet = petRepository.save(pet);

        Appointment toCreate = new Appointment();
        toCreate.setPetId(pet.getId());
        toCreate.setVetId(request.getVetId());
        toCreate.setDateTime(request.getDateTime());
        toCreate.setReason(request.getReason());
        Appointment appointment = appointmentService.create(toCreate);

        return new AdmitPetWithAppointmentResponse(pet, appointment);
    }

    /**
     * Бизнес-операция: завершить визит и оформить назначение в одной транзакции (Appointment + Treatment).
     */
    @Transactional
    public Treatment completeVisitWithTreatment(CompleteVisitWithTreatmentRequest request) {
        if (request.getAppointmentId() == null) {
            throw new IllegalArgumentException("appointmentId is required");
        }

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment with id " + request.getAppointmentId() + " not found"));

        if (treatmentRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment with id " + request.getAppointmentId() + " already has a treatment");
        }

        LocalDateTime now = LocalDateTime.now();
        if (appointment.getDateTime() != null && appointment.getDateTime().isAfter(now)) {
            throw new IllegalArgumentException("Treatment can be created only after the appointment time");
        }

        Treatment treatment = new Treatment();
        treatment.setAppointmentId(request.getAppointmentId());
        treatment.setProceduresAndMedications(request.getProceduresAndMedications());
        treatment.setNotes(request.getNotes());
        treatment = treatmentRepository.save(treatment);

        appointment.setCompleted(true);
        appointmentRepository.save(appointment);

        return treatment;
    }

    /**
     * Бизнес-операция: сводка по владельцу и его питомцам (Owner + Pet).
     */
    @Transactional(readOnly = true)
    public OwnerSummaryResponse getOwnerSummary(Long ownerId) {
        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner with id " + ownerId + " not found"));

        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        List<OwnerSummaryResponse.PetBrief> briefs = pets.stream()
                .map(p -> new OwnerSummaryResponse.PetBrief(p.getId(), p.getName(), p.getSpecies()))
                .toList();

        OwnerSummaryResponse response = new OwnerSummaryResponse();
        response.setId(owner.getId());
        response.setName(owner.getName());
        response.setLastName(owner.getLastName());
        response.setPhone(owner.getPhone());
        response.setEmail(owner.getEmail());
        response.setPets(briefs);
        return response;
    }

    /**
     * Бизнес-операция: расписание врача с именами питомцев (Vet + Appointment + Pet).
     */
    @Transactional(readOnly = true)
    public List<VetScheduleRow> getVetSchedule(Long vetId, LocalDateTime from, LocalDateTime to) {
        if (!vetRepository.existsById(vetId)) {
            throw new IllegalArgumentException("Vet with id " + vetId + " not found");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to query parameters are required (ISO-8601 date-time)");
        }
        if (!to.isAfter(from)) {
            throw new IllegalArgumentException("to must be after from");
        }

        List<Appointment> appointments = appointmentRepository.findByVetIdAndDateTimeBetweenOrderByDateTimeAsc(vetId, from, to);
        List<VetScheduleRow> rows = new ArrayList<>();
        for (Appointment a : appointments) {
            String petName = petRepository.findById(a.getPetId())
                    .map(Pet::getName)
                    .orElse("?");
            rows.add(new VetScheduleRow(a.getId(), a.getDateTime(), a.getPetId(), petName, a.getReason(), a.isCompleted()));
        }
        return rows;
    }

    /**
     * Бизнес-операция: отмена визита (удаление записи; связанное назначение удаляется каскадом в БД).
     */
    @Transactional
    public void cancelAppointment(CancelAppointmentRequest request) {
        if (request.getAppointmentId() == null) {
            throw new IllegalArgumentException("appointmentId is required");
        }
        if (!appointmentRepository.existsById(request.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment with id " + request.getAppointmentId() + " not found");
        }
        appointmentRepository.deleteById(request.getAppointmentId());
    }
}
