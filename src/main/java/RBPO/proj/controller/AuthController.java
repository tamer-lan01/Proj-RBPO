package RBPO.proj.controller;

import RBPO.proj.dto.RegistrationRequest;
import RBPO.proj.model.AppUser;
import RBPO.proj.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String ADMIN_REGISTER_SECRET_HEADER = "X-Admin-Register-Secret";

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
        try {
            return okRegistration(registrationService.registerUser(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private ResponseEntity<?> okRegistration(AppUser user) {
        return ResponseEntity.ok(Map.of(
                "message", "Регистрация выполнена",
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }

    /**
     * Для POST/PUT/DELETE (кроме публичных register/*) передавайте заголовок X-XSRF-TOKEN.
     */
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        if (token == null) {
            return Map.of();
        }
        return Map.of(
                "headerName", token.getHeaderName(),
                "parameterName", token.getParameterName(),
                "token", token.getToken()
        );
    }
}
