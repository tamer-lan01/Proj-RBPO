package RBPO.proj.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import RBPO.proj.model.Appointment;
import RBPO.proj.model.Vet;
import RBPO.proj.service.AppointmentService;
import RBPO.proj.service.VetService;

import java.util.List;

@RestController
@RequestMapping("/api/vets")
public class VetController {

    private final VetService vetService;
    private final AppointmentService appointmentService;

    public VetController(VetService vetService, AppointmentService appointmentService) {
        this.vetService = vetService;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Vet> create(@RequestBody Vet vet) {
        Vet created = vetService.create(vet);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vet> getById(@PathVariable Long id) {
        return vetService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET: все визиты у данного врача */
    @GetMapping("/{vetId}/appointments")
    public ResponseEntity<List<Appointment>> getAppointmentsForVet(@PathVariable Long vetId) {
        if (!vetService.exists(vetId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appointmentService.getByVetId(vetId));
    }

    @GetMapping
    public List<Vet> getAll() {
        return vetService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vet> update(@PathVariable Long id, @RequestBody Vet vet) {
        return vetService.update(id, vet)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!vetService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
