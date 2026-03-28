package RBPO.proj.dto;

import java.time.LocalDateTime;

public class VetScheduleRow {
    private Long appointmentId;
    private LocalDateTime dateTime;
    private Long petId;
    private String petName;
    private String reason;
    private boolean completed;

    public VetScheduleRow() {
    }

    public VetScheduleRow(Long appointmentId, LocalDateTime dateTime, Long petId, String petName, String reason, boolean completed) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.petId = petId;
        this.petName = petName;
        this.reason = reason;
        this.completed = completed;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
