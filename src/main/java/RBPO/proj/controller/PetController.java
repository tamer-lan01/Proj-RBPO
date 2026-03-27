package RBPO.proj.controller;

import RBPO.proj.debug.DebugLog;
import RBPO.proj.dto.PetInfo;
import RBPO.proj.model.Appointment;
import RBPO.proj.model.Pet;
import RBPO.proj.service.AppointmentService;
import RBPO.proj.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final AppointmentService appointmentService;

    public PetController(PetService petService, AppointmentService appointmentService) {
        this.petService = petService;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Pet pet) {
        try {
            // #region agent log
            DebugLog.log(
                    "pre-fix",
                    "B",
                    "PetController.java:create",
                    "POST /api/pets received",
                    Map.of(
                            "petNamePresent", pet != null && pet.getName() != null,
                            "ownerId", pet != null ? pet.getOwnerId() : null
                    )
            );
            // #endregion

            Pet created = petService.create(pet);
            PetInfo info = petService.toPetInfo(created);

            // #region agent log
            DebugLog.log(
                    "pre-fix",
                    "B",
                    "PetController.java:create",
                    "Pet created response",
                    Map.of(
                            "petId", info.getId(),
                            "petName", info.getName(),
                            "ownerLastName", info.getOwnerLastName()
                    )
            );
            // #endregion

            return ResponseEntity.status(HttpStatus.CREATED).body(info);
        } catch (IllegalArgumentException e) {
            // #region agent log
            DebugLog.log(
                    "pre-fix",
                    "B",
                    "PetController.java:create",
                    "POST /api/pets validation error",
                    Map.of("error", e.getMessage())
            );
            // #endregion

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** GET: все визиты конкретного питомца */
    @GetMapping("/{petId}/appointments")
    public ResponseEntity<List<Appointment>> getAppointmentsForPet(@PathVariable Long petId) {
        if (!petService.exists(petId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appointmentService.getByPetId(petId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetInfo> getById(@PathVariable Long id) {
        return petService.getById(id)
                .map(petService::toPetInfo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PetInfo> getAll(@RequestParam(required = false) Long ownerId) {
        if (ownerId != null) {
            return petService.getPetInfosByOwnerId(ownerId);
        }
        return petService.getAllPetInfos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Pet pet) {
        try {
            return petService.update(id, pet)
                    .map(petService::toPetInfo)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!petService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
