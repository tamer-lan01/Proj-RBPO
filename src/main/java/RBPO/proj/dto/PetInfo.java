package RBPO.proj.dto;

public class PetInfo {
    private Long id;
    private String name;
    private String ownerLastName;

    public PetInfo() {
    }

    public PetInfo(Long id, String name, String ownerLastName) {
        this.id = id;
        this.name = name;
        this.ownerLastName = ownerLastName;
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

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }
}
