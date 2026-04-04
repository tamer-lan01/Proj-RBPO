package RBPO.proj.service;

import RBPO.proj.dto.RegistrationRequest;
import RBPO.proj.model.AppUser;
import RBPO.proj.repository.AppUserRepository;
import RBPO.proj.security.AppRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Регистрация отдельно для USER и для ADMIN. Пользователей в data.sql нет.
 */
@Service
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminRegisterSecret;

    public RegistrationService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.admin-register-secret}") String adminRegisterSecret) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRegisterSecret = adminRegisterSecret;
    }

    @Transactional
    public AppUser registerUser(RegistrationRequest request) {
        return registerWithRole(request, AppRole.USER);
    }

    private AppUser registerWithRole(RegistrationRequest request, AppRole role) {
        if (appUserRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        AppUser user = new AppUser();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        return appUserRepository.save(user);
    }
}
