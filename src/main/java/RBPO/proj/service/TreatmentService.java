package RBPO.proj.service;

import RBPO.proj.model.Appointment;
import RBPO.proj.model.Treatment;
import RBPO.proj.repository.AppointmentRepository;
import RBPO.proj.repository.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final AppointmentRepository appointmentRepository;

    public TreatmentService(TreatmentRepository treatmentRepository, AppointmentRepository appointmentRepository) {
        this.treatmentRepository = treatmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public Treatment create(Treatment treatment) {
        if (treatment.getAppointmentId() == null) {
            throw new IllegalArgumentException("appointmentId is required");
        }

        Appointment appointment = appointmentRepository.findById(treatment.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment with id " + treatment.getAppointmentId() + " not found"));

        LocalDate today = LocalDate.now();
        boolean dayOk = appointment.getVisitDate() == null || !appointment.getVisitDate().isAfter(today);
        if (!appointment.isCompleted() && !dayOk) {
            throw new IllegalArgumentException("Treatment can be created only on or after the appointment day");
        }

        if (treatmentRepository.existsByAppointmentId(treatment.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment with id " + treatment.getAppointmentId() + " already has a treatment");
        }

        Treatment entity = new Treatment();
        entity.setAppointmentId(treatment.getAppointmentId());
        entity.setProceduresAndMedications(treatment.getProceduresAndMedications());
        entity.setNotes(treatment.getNotes());
        return treatmentRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Treatment> getById(Long id) {
        return treatmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Treatment> getAll() {
        return treatmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Treatment> getByAppointmentId(Long appointmentId) {
        return treatmentRepository.findByAppointmentId(appointmentId)
                .map(List::of)
                .orElseGet(List::of);
    }

    @Transactional
    public Optional<Treatment> update(Long id, Treatment treatment) {
        return treatmentRepository.findById(id).map(existing -> {
            existing.setProceduresAndMedications(treatment.getProceduresAndMedications());
            existing.setNotes(treatment.getNotes());
            return treatmentRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!treatmentRepository.existsById(id)) {
            return false;
        }
        treatmentRepository.deleteById(id);
        return true;
    }
}
