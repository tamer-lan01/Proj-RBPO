package RBPO.proj.service;

import RBPO.proj.model.Owner;
import RBPO.proj.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public Owner create(Owner owner) {
        Owner entity = new Owner();
        entity.setName(owner.getName());
        entity.setLastName(owner.getLastName());
        entity.setPhone(owner.getPhone());
        entity.setEmail(owner.getEmail());
        return ownerRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Owner> getById(Long id) {
        return ownerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Owner> getAll() {
        return ownerRepository.findAll();
    }

    @Transactional
    public Optional<Owner> update(Long id, Owner owner) {
        return ownerRepository.findById(id).map(existing -> {
            existing.setName(owner.getName());
            existing.setLastName(owner.getLastName());
            existing.setPhone(owner.getPhone());
            existing.setEmail(owner.getEmail());
            return ownerRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!ownerRepository.existsById(id)) {
            return false;
        }
        ownerRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return ownerRepository.existsById(id);
    }
}
