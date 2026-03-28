package RBPO.proj.dto;

import RBPO.proj.model.Appointment;
import RBPO.proj.model.Pet;

public class AdmitPetWithAppointmentResponse {
    private Pet pet;
    private Appointment appointment;

    public AdmitPetWithAppointmentResponse() {
    }

    public AdmitPetWithAppointmentResponse(Pet pet, Appointment appointment) {
        this.pet = pet;
        this.appointment = appointment;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
