package RBPO.proj.controller;

import RBPO.proj.dto.PetInfo;
import RBPO.proj.model.Owner;
import RBPO.proj.service.OwnerService;
import RBPO.proj.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;
    private final PetService petService;

    public OwnerController(OwnerService ownerService, PetService petService) {
        this.ownerService = ownerService;
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        Owner created = ownerService.create(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> getById(@PathVariable Long id) {
        return ownerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET: все питомцы данного владельца */
    @GetMapping("/{ownerId}/pets")
    public ResponseEntity<List<PetInfo>> getPetsByOwner(@PathVariable Long ownerId) {
        if (!ownerService.exists(ownerId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(petService.getPetInfosByOwnerId(ownerId));
    }

    @GetMapping
    public List<Owner> getAll() {
        return ownerService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        return ownerService.update(id, owner)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ownerService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
