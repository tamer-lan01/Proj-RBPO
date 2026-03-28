package RBPO.proj.service;

import RBPO.proj.model.Appointment;
import RBPO.proj.repository.AppointmentRepository;
import RBPO.proj.repository.PetRepository;
import RBPO.proj.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PetRepository petRepository,
                              VetRepository vetRepository) {
        this.appointmentRepository = appointmentRepository;
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
    }

    @Transactional
    public Appointment create(Appointment appointment) {
        requireValidAppointment(appointment.getPetId(), appointment.getVetId(), appointment.getDateTime(), null);
        Appointment entity = new Appointment();
        entity.setPetId(appointment.getPetId());
        entity.setVetId(appointment.getVetId());
        entity.setDateTime(appointment.getDateTime());
        entity.setReason(appointment.getReason());
        entity.setCompleted(false);
        return appointmentRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Appointment> getById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Appointment> getByPetId(Long petId) {
        return appointmentRepository.findByPetId(petId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getByVetId(Long vetId) {
        return appointmentRepository.findByVetId(vetId);
    }

    @Transactional
    public Optional<Appointment> update(Long id, Appointment appointment) {
        return appointmentRepository.findById(id).map(existing -> {
            Long newPetId = appointment.getPetId() != null ? appointment.getPetId() : existing.getPetId();
            Long newVetId = appointment.getVetId() != null ? appointment.getVetId() : existing.getVetId();
            LocalDateTime newDateTime = appointment.getDateTime() != null ? appointment.getDateTime() : existing.getDateTime();

            requireValidAppointment(newPetId, newVetId, newDateTime, id);

            existing.setPetId(newPetId);
            existing.setVetId(newVetId);
            existing.setDateTime(newDateTime);
            existing.setReason(appointment.getReason());
            return appointmentRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            return false;
        }
        appointmentRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return appointmentRepository.existsById(id);
    }

    private void requireValidAppointment(Long petId, Long vetId, LocalDateTime dateTime, Long ignoreAppointmentId) {
        if (petId == null || !petRepository.existsById(petId)) {
            throw new IllegalArgumentException("Pet with id " + petId + " not found");
        }
        if (vetId == null || !vetRepository.existsById(vetId)) {
            throw new IllegalArgumentException("Vet with id " + vetId + " not found");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime is required");
        }
        boolean slotBusy = ignoreAppointmentId == null
                ? appointmentRepository.existsByVetIdAndDateTime(vetId, dateTime)
                : appointmentRepository.existsByVetIdAndDateTimeAndIdNot(vetId, dateTime, ignoreAppointmentId);
        if (slotBusy) {
            throw new IllegalArgumentException("Vet with id " + vetId + " already has appointment at " + dateTime);
        }
    }
}
