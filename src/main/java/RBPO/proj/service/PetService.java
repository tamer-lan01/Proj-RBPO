package RBPO.proj.service;

import org.springframework.stereotype.Service;
import RBPO.proj.dto.PetInfo;
import RBPO.proj.model.Pet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {
    private final Map<Long, Pet> storage = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    private final OwnerService ownerService;

    public PetService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public Pet create(Pet pet) {
        if (pet.getOwnerId() == null || !ownerService.exists(pet.getOwnerId())) {
            throw new IllegalArgumentException("Owner with id " + pet.getOwnerId() + " not found");
        }
        Pet entity = new Pet(null, pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getOwnerId());
        entity.setId(nextId.getAndIncrement());
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<Pet> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Pet> getAll() {
        return List.copyOf(storage.values());
    }

    public List<Pet> getByOwnerId(Long ownerId) {
        return storage.values().stream()
                .filter(p -> ownerId.equals(p.getOwnerId()))
                .toList();
    }

    public Optional<Pet> update(Long id, Pet pet) {
        Pet existing = storage.get(id);
        if (existing == null) return Optional.empty();
        if (pet.getOwnerId() != null) {
            if (!ownerService.exists(pet.getOwnerId())) {
                throw new IllegalArgumentException("Owner with id " + pet.getOwnerId() + " not found");
            }
            existing.setOwnerId(pet.getOwnerId());
        }
        existing.setName(pet.getName());
        existing.setSpecies(pet.getSpecies());
        existing.setBreed(pet.getBreed());
        return Optional.of(existing);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public boolean exists(Long id) {
        return storage.containsKey(id);
    }

    /** Для ответов API: id питомца, имя, фамилия хозяина */
    public PetInfo toPetInfo(Pet pet) {
        String ownerLastName = null;
        if (pet.getOwnerId() != null) {
            ownerLastName = ownerService.getById(pet.getOwnerId())
                    .map(o -> o.resolveLastName())
                    .orElse(null);
        }
        return new PetInfo(pet.getId(), pet.getName(), ownerLastName);
    }

    public List<PetInfo> getAllPetInfos() {
        return getAll().stream().map(this::toPetInfo).toList();
    }

    public List<PetInfo> getPetInfosByOwnerId(Long ownerId) {
        return getByOwnerId(ownerId).stream().map(this::toPetInfo).toList();
    }
}
