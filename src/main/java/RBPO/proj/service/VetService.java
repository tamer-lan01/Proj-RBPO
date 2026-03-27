package RBPO.proj.service;

import org.springframework.stereotype.Service;
import RBPO.proj.model.Vet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VetService {
    private final Map<Long, Vet> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public Vet create(Vet vet) {
        Vet entity = new Vet(null, vet.getName(), vet.getSpecialization());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Vet> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Vet> getAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Vet> update(Long id, Vet vet) {
        Vet existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setName(vet.getName());
        existing.setSpecialization(vet.getSpecialization());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }
}
