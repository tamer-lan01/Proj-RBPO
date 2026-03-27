package RBPO.proj.model;

public class Treatment {
    private Long id;
    private Long appointmentId;
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
