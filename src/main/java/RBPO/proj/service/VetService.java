package RBPO.proj.service;

import RBPO.proj.model.Vet;
import RBPO.proj.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VetService {

    private final VetRepository vetRepository;

    public VetService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    @Transactional
    public Vet create(Vet vet) {
        Vet entity = new Vet();
        entity.setName(vet.getName());
        entity.setSpecialization(vet.getSpecialization());
        return vetRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Vet> getById(Long id) {
        return vetRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Vet> getAll() {
        return vetRepository.findAll();
    }

    @Transactional
    public Optional<Vet> update(Long id, Vet vet) {
        return vetRepository.findById(id).map(existing -> {
            existing.setName(vet.getName());
            existing.setSpecialization(vet.getSpecialization());
            return vetRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!vetRepository.existsById(id)) {
            return false;
        }
        vetRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return vetRepository.existsById(id);
    }
}
