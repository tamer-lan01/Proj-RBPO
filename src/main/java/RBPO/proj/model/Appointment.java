package RBPO.proj.model;

import java.time.LocalDateTime;

public class Appointment {
    private Long id;
    private Long petId;
    private Long vetId;
    private LocalDateTime dateTime;
    private String reason;

    public Appointment() {
    }

    public Appointment(Long id, Long petId, Long vetId, LocalDateTime dateTime, String reason) {
        this.id = id;
        this.petId = petId;
        this.vetId = vetId;
        this.dateTime = dateTime;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getVetId() {
        return vetId;
    }

    public void setVetId(Long vetId) {
        this.vetId = vetId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
