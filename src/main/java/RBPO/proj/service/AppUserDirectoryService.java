package RBPO.proj.service;

import RBPO.proj.dto.AppUserPublicDto;
import RBPO.proj.model.AppUser;
import RBPO.proj.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserDirectoryService {

    private static final String PASSWORD_INFO =
            "В БД хранится только BCrypt; исходный пароль не возвращается. "
                    + "Для входа используйте пароль, заданный при регистрации "
                    + "(в Postman после Register он в переменной коллекции basicPassword).";

    private final AppUserRepository appUserRepository;

    public AppUserDirectoryService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public List<AppUserPublicDto> listAllForAdmin() {
        return appUserRepository.findAllByOrderByIdAsc().stream()
                .map(this::toDto)
                .toList();
    }

    private AppUserPublicDto toDto(AppUser u) {
        AppUserPublicDto dto = new AppUserPublicDto();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setLogin(u.getEmail());
        dto.setRole(u.getRole());
        dto.setPassword(PASSWORD_INFO);
        return dto;
    }
}
