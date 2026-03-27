package RBPO.proj.service;

import org.springframework.stereotype.Service;
import RBPO.proj.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AppointmentService {
    private final Map<Long, Appointment> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    private final PetService petService;
    private final VetService vetService;

    public AppointmentService(PetService petService, VetService vetService) {
        this.petService = petService;
        this.vetService = vetService;
    }

    public Appointment create(Appointment appointment) {
        requireValidAppointment(appointment.getPetId(), appointment.getVetId(), appointment.getDateTime(), null);
        Appointment entity = new Appointment(null, appointment.getPetId(), appointment.getVetId(), appointment.getDateTime(), appointment.getReason());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Appointment> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Appointment> getAll() {
        return List.copyOf(storage.values());
    }

    public List<Appointment> getByPetId(Long petId) {
        return storage.values().stream()
                .filter(a -> petId.equals(a.getPetId()))
                .toList();
    }

    public List<Appointment> getByVetId(Long vetId) {
        return storage.values().stream()
                .filter(a -> vetId.equals(a.getVetId()))
                .toList();
    }

    public Optional<Appointment> update(Long id, Appointment appointment) {
        Appointment existing = storage.get(id);
        if (existing == null) return Optional.empty();

        Long newPetId = appointment.getPetId() != null ? appointment.getPetId() : existing.getPetId();
        Long newVetId = appointment.getVetId() != null ? appointment.getVetId() : existing.getVetId();
        LocalDateTime newDateTime = appointment.getDateTime() != null ? appointment.getDateTime() : existing.getDateTime();

        requireValidAppointment(newPetId, newVetId, newDateTime, id);

        existing.setPetId(newPetId);
        existing.setVetId(newVetId);
        existing.setDateTime(newDateTime);
        existing.setReason(appointment.getReason());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }

    private void requireValidAppointment(Long petId, Long vetId, LocalDateTime dateTime, Long ignoreAppointmentId) {
        if (petId == null || !petService.exists(petId)) {
            throw new IllegalArgumentException("Pet with id " + petId + " not found");
        }
        if (vetId == null || !vetService.exists(vetId)) {
            throw new IllegalArgumentException("Vet with id " + vetId + " not found");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime is required");
        }
        boolean slotBusy = storage.values().stream().anyMatch(a ->
                (ignoreAppointmentId == null || !ignoreAppointmentId.equals(a.getId()))
                        && vetId.equals(a.getVetId())
                        && dateTime.equals(a.getDateTime())
        );
        if (slotBusy) {
            throw new IllegalArgumentException("Vet with id " + vetId + " already has appointment at " + dateTime);
        }
    }
}
