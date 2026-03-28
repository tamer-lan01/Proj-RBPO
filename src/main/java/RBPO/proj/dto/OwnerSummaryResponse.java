package RBPO.proj.dto;

import java.util.List;

public class OwnerSummaryResponse {
    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private List<PetBrief> pets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PetBrief> getPets() {
        return pets;
    }

    public void setPets(List<PetBrief> pets) {
        this.pets = pets;
    }

    public static class PetBrief {
        private Long id;
        private String name;
        private String species;

        public PetBrief() {
        }

        public PetBrief(Long id, String name, String species) {
            this.id = id;
            this.name = name;
            this.species = species;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpecies() {
            return species;
        }

        public void setSpecies(String species) {
            this.species = species;
        }
    }
}
