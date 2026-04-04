package RBPO.proj.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfException;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Регистрация вынесена в {@link WebSecurityCustomizer}: путь не проходит через цепочки фильтров —
 * Postman не может навесить CSRF/Basic на {@code /api/auth/register/**}. Остальное: две цепочки + Basic + CSRF.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RestAuthEntryPoint restAuthEntryPoint;

    public SecurityConfig(RestAuthEntryPoint restAuthEntryPoint) {
        this.restAuthEntryPoint = restAuthEntryPoint;
    }

    /**
     * Полностью исключает регистрацию из Spring Security (ни CSRF, ни Basic, ни порядок цепочек).
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/api/auth/register/**");
    }

    @Bean
    @Order(0)
    public SecurityFilterChain publicApiChain(HttpSecurity http) throws Exception {
        // В этой версии Spring Security securityMatcher принимает Ant-шаблоны как String..., не RequestMatcher
        http.securityMatcher("/api/hello", "/api/auth/csrf")
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securedApiChain(HttpSecurity http) throws Exception {
        // Совпадает и с /api/hello — но цепочка 0 (Order меньше) выбирается первой для hello и csrf
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            boolean csrf = isCsrfRelated(accessDeniedException);
                            String msg = csrf
                                    ? "Ошибка CSRF: для защищённых POST/PUT/DELETE сначала GET /api/auth/csrf. "
                                            + "Регистрация /api/auth/register/** CSRF не требует."
                                    : "Доступ запрещён для вашей роли";
                            String esc = msg.replace("\\", "\\\\").replace("\"", "\\\"");
                            response.getWriter().write("{\"error\":\"" + esc + "\"}");
                        }));
        return http.build();
    }

    private static boolean isCsrfRelated(Throwable ex) {
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (t instanceof CsrfException) {
                return true;
            }
            String name = t.getClass().getName();
            if (name.contains("Csrf")) {
                return true;
            }
        }
        return false;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
