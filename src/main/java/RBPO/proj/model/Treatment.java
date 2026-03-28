package RBPO.proj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "treatments")
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false, unique = true)
    private Long appointmentId;

    @Column(name = "procedures_and_medications")
    private String proceduresAndMedications;

    private String notes;

    public Treatment() {
    }

    public Treatment(Long id, Long appointmentId, String proceduresAndMedications, String notes) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.proceduresAndMedications = proceduresAndMedications;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getProceduresAndMedications() {
        return proceduresAndMedications;
    }

    public void setProceduresAndMedications(String proceduresAndMedications) {
        this.proceduresAndMedications = proceduresAndMedications;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
