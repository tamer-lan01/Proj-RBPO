package RBPO.proj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import RBPO.proj.model.Appointment;
import RBPO.proj.model.Treatment;
import RBPO.proj.service.AppointmentService;
import RBPO.proj.service.TreatmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TreatmentService treatmentService;

    public AppointmentController(AppointmentService appointmentService, TreatmentService treatmentService) {
        this.appointmentService = appointmentService;
        this.treatmentService = treatmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Appointment appointment) {
        try {
            Appointment created = appointmentService.create(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> getById(@PathVariable Long id) {
        return appointmentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET: все назначения по данному визиту */
    @GetMapping("/{appointmentId}/treatments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Treatment>> getTreatmentsForAppointment(@PathVariable Long appointmentId) {
        if (!appointmentService.exists(appointmentId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(treatmentService.getByAppointmentId(appointmentId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Appointment> getAll(@RequestParam(required = false) Long petId,
                                   @RequestParam(required = false) Long vetId) {
        if (petId != null) return appointmentService.getByPetId(petId);
        if (vetId != null) return appointmentService.getByVetId(vetId);
        return appointmentService.getAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Appointment appointment) {
        try {
            return appointmentService.update(id, appointment)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!appointmentService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
