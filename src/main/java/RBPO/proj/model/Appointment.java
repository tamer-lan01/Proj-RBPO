package RBPO.proj.model;

import RBPO.proj.jackson.VisitDateDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "vet_id", nullable = false)
    private Long vetId;

    /** День приёма (без времени): один слот на врача в календарный день. */
    @Column(name = "visit_date", nullable = false)
    @JsonAlias("dateTime")
    @JsonDeserialize(using = VisitDateDeserializer.class)
    private LocalDate visitDate;

    private String reason;

    @Column(nullable = false)
    private boolean completed;

    public Appointment() {
    }

    public Appointment(Long id, Long petId, Long vetId, LocalDate visitDate, String reason) {
        this.id = id;
        this.petId = petId;
        this.vetId = vetId;
        this.visitDate = visitDate;
        this.reason = reason;
        this.completed = false;
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

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
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
