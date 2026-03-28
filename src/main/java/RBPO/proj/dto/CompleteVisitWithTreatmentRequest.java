package RBPO.proj.dto;

public class CompleteVisitWithTreatmentRequest {
    private Long appointmentId;
    private String proceduresAndMedications;
    private String notes;

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
