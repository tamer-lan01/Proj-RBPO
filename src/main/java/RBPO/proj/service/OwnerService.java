package RBPO.proj.service;

import org.springframework.stereotype.Service;
import RBPO.proj.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OwnerService {
    private final Map<Long, Owner> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public Owner create(Owner owner) {
        Owner entity = new Owner(null, owner.getName(), owner.getLastName(), owner.getPhone(), owner.getEmail());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Owner> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Owner> getAll() {
        return List.copyOf(storage.values());
    }

    public Optional<Owner> update(Long id, Owner owner) {
        Owner existing = storage.get(id);
        if (existing == null) return Optional.empty();
        existing.setName(owner.getName());
        existing.setLastName(owner.getLastName());
        existing.setPhone(owner.getPhone());
        existing.setEmail(owner.getEmail());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }
}
