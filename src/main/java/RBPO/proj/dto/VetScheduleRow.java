package RBPO.proj.dto;

import java.time.LocalDate;

public class VetScheduleRow {
    private Long appointmentId;
    private LocalDate visitDate;
    private Long petId;
    private String petName;
    private String reason;
    private boolean completed;

    public VetScheduleRow(Long appointmentId, LocalDate visitDate, Long petId, String petName, String reason, boolean completed) {
        this.appointmentId = appointmentId;
        this.visitDate = visitDate;
        this.petId = petId;
        this.petName = petName;
        this.reason = reason;
        this.completed = completed;
    }

    public VetScheduleRow() {
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
