package RBPO.proj.model;

public class Owner {
    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String email;

    public Owner() {
    }

    public Owner(Long id, String name, String lastName, String phone, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
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

    public String resolveLastName() {
        if (lastName != null && !lastName.isBlank()) return lastName;
        if (name == null) return null;
        String trimmed = name.trim();
        if (trimmed.isEmpty()) return null;
        int idx = trimmed.lastIndexOf(' ');
        return idx >= 0 ? trimmed.substring(idx + 1) : trimmed;
    }
}
