package RBPO.proj.controller;

import RBPO.proj.dto.AdmitPetWithAppointmentRequest;
import RBPO.proj.dto.AdmitPetWithAppointmentResponse;
import RBPO.proj.dto.CancelAppointmentRequest;
import RBPO.proj.dto.CompleteVisitWithTreatmentRequest;
import RBPO.proj.dto.OwnerSummaryResponse;
import RBPO.proj.dto.VetScheduleRow;
import RBPO.proj.model.Treatment;
import RBPO.proj.service.OperationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/admit-pet-with-appointment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> admitPetWithAppointment(@RequestBody AdmitPetWithAppointmentRequest request) {
        try {
            AdmitPetWithAppointmentResponse body = operationService.admitPetWithAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/complete-visit-with-treatment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> completeVisitWithTreatment(@RequestBody CompleteVisitWithTreatmentRequest request) {
        try {
            Treatment treatment = operationService.completeVisitWithTreatment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(treatment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/owners/{ownerId}/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> ownerSummary(@PathVariable Long ownerId) {
        try {
            OwnerSummaryResponse body = operationService.getOwnerSummary(ownerId);
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/vets/{vetId}/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> vetSchedule(
            @PathVariable Long vetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<VetScheduleRow> rows = operationService.getVetSchedule(vetId, from, to);
            return ResponseEntity.ok(rows);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel-appointment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelAppointment(@RequestBody CancelAppointmentRequest request) {
        try {
            operationService.cancelAppointment(request);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
