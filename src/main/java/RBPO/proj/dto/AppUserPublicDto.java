package RBPO.proj.dto;

import RBPO.proj.security.AppRole;

/**
 * Публичное представление учётной записи для администратора.
 * Пароль в открытом виде из БД получить нельзя (только BCrypt-хэш).
 */
public class AppUserPublicDto {

    private Long id;
    private String name;
    /** Логин для HTTP Basic — email. */
    private String login;
    private AppRole role;
    /**
     * Текстовое пояснение: исходный пароль не возвращается по соображениям безопасности.
     */
    private String password;

    public AppUserPublicDto() {
    }

    public AppUserPublicDto(Long id, String name, String login, AppRole role, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.role = role;
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public AppRole getRole() {
        return role;
    }

    public void setRole(AppRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
