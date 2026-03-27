package RBPO.proj.model;

public class Pet {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private Long ownerId;

    public Pet() {
    }

    public Pet(Long id, String name, String species, String breed, Long ownerId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.ownerId = ownerId;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
