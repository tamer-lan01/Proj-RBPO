package RBPO.proj.service;

import RBPO.proj.dto.PetInfo;
import RBPO.proj.model.Owner;
import RBPO.proj.model.Pet;
import RBPO.proj.repository.OwnerRepository;
import RBPO.proj.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetService(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public Pet create(Pet pet) {
        if (pet.getOwnerId() == null || !ownerRepository.existsById(pet.getOwnerId())) {
            throw new IllegalArgumentException("Owner with id " + pet.getOwnerId() + " not found");
        }
        Pet entity = new Pet();
        entity.setName(pet.getName());
        entity.setSpecies(pet.getSpecies());
        entity.setBreed(pet.getBreed());
        entity.setOwnerId(pet.getOwnerId());
        return petRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Pet> getById(Long id) {
        return petRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Pet> getByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public Optional<Pet> update(Long id, Pet pet) {
        return petRepository.findById(id).map(existing -> {
            if (pet.getOwnerId() != null) {
                if (!ownerRepository.existsById(pet.getOwnerId())) {
                    throw new IllegalArgumentException("Owner with id " + pet.getOwnerId() + " not found");
                }
                existing.setOwnerId(pet.getOwnerId());
            }
            existing.setName(pet.getName());
            existing.setSpecies(pet.getSpecies());
            existing.setBreed(pet.getBreed());
            return petRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!petRepository.existsById(id)) {
            return false;
        }
        petRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return petRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public PetInfo toPetInfo(Pet pet) {
        String ownerLastName = null;
        if (pet.getOwnerId() != null) {
            ownerLastName = ownerRepository.findById(pet.getOwnerId())
                    .map(Owner::resolveLastName)
                    .orElse(null);
        }
        return new PetInfo(pet.getId(), pet.getName(), ownerLastName);
    }

    @Transactional(readOnly = true)
    public List<PetInfo> getAllPetInfos() {
        return getAll().stream().map(this::toPetInfo).toList();
    }

    @Transactional(readOnly = true)
    public List<PetInfo> getPetInfosByOwnerId(Long ownerId) {
        return getByOwnerId(ownerId).stream().map(this::toPetInfo).toList();
    }
}
