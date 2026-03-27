package RBPO.proj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import RBPO.proj.model.Treatment;
import RBPO.proj.service.TreatmentService;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Treatment treatment) {
        try {
            Treatment created = treatmentService.create(treatment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getById(@PathVariable Long id) {
        return treatmentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Treatment> getAll(@RequestParam(required = false) Long appointmentId) {
        if (appointmentId != null) return treatmentService.getByAppointmentId(appointmentId);
        return treatmentService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Treatment> update(@PathVariable Long id, @RequestBody Treatment treatment) {
        return treatmentService.update(id, treatment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!treatmentService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
