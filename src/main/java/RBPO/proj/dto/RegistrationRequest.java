package RBPO.proj.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Регистрация учётной записи. Пароль: минимум 8 символов, цифра, строчная и прописная буква, спецсимвол.
 */
public class RegistrationRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\-_.?])(?=\\S+$).{8,128}$",
            message = "Пароль: 8–128 символов, без пробелов, нужны цифра, строчная и заглавная буква, спецсимвол из @#$%^&+=!-_.?"
    )
    private String password;

    public RegistrationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
