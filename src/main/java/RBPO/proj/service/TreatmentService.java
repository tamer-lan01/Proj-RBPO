package RBPO.proj.service;

import org.springframework.stereotype.Service;
import RBPO.proj.model.Appointment;
import RBPO.proj.model.Treatment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TreatmentService {
    private final Map<Long, Treatment> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    private final AppointmentService appointmentService;

    public TreatmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Treatment create(Treatment treatment) {
        if (treatment.getAppointmentId() == null) {
            throw new IllegalArgumentException("appointmentId is required");
        }

        Appointment appointment = appointmentService.getById(treatment.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment with id " + treatment.getAppointmentId() + " not found"));

        LocalDateTime now = LocalDateTime.now();
        if (appointment.getDateTime() != null && appointment.getDateTime().isAfter(now)) {
            throw new IllegalArgumentException("Treatment can be created only after the appointment is completed");
        }

        boolean alreadyHasTreatment = storage.values().stream()
                .anyMatch(t -> treatment.getAppointmentId().equals(t.getAppointmentId()));
        if (alreadyHasTreatment) {
            throw new IllegalArgumentException("Appointment with id " + treatment.getAppointmentId() + " already has a treatment");
        }

        Treatment entity = new Treatment(null, treatment.getAppointmentId(), treatment.getProceduresAndMedications(), treatment.getNotes());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Treatment> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Treatment> getAll() {
        return List.copyOf(storage.values());
    }

    public List<Treatment> getByAppointmentId(Long appointmentId) {
        return storage.values().stream()
                .filter(t -> appointmentId.equals(t.getAppointmentId()))
                .toList();
    }

    public Optional<Treatment> update(Long id, Treatment treatment) {
        Treatment existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setProceduresAndMedications(treatment.getProceduresAndMedications());
        existing.setNotes(treatment.getNotes());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }
}
