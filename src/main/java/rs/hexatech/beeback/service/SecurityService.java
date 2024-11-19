package rs.hexatech.beeback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rs.hexatech.beeback.domain.Authority;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.UserRepository;
import rs.hexatech.beeback.security.AuthoritiesConstants;
import rs.hexatech.beeback.security.SecurityUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {

        Optional<String> loginOptional = SecurityUtils.getCurrentUserLogin();
        Optional<User> userOptional = userRepository.findOneByLogin(loginOptional.orElseThrow(() -> new AccessDeniedException("The user is not logged in!")));
        User user = userOptional.orElseThrow(() -> new AccessDeniedException("The current logged in user not found"));
        if (!user.isActivated()) {
            throw new AccessDeniedException("The currently logged in user is not activated");
        }

        saveUserActivityTimestamp(user);

        return user;
    }

    public Boolean isCurrentAdminRole() {
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
    }

    public Boolean isUserAdminRole(User user) {
        if (Objects.isNull(user)) {
            return Boolean.FALSE;
        }
        return user.getAuthorities().stream().map(Authority::getName)
                .anyMatch(authority -> authority.equals(AuthoritiesConstants.ADMIN));
    }

    public Boolean isCurrentUserRole() {
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER);
    }

    public Boolean isUserUserRole(User user) {
        if (Objects.isNull(user)) {
            return Boolean.FALSE;
        }
        return user.getAuthorities().stream().map(Authority::getName)
                .anyMatch(authority -> authority.equals(AuthoritiesConstants.USER));
    }

    public void saveUserActivityTimestamp() {
        saveUserActivityTimestamp(getCurrentUser());
    }

    public void saveUserActivityTimestamp(User currentUser) {
        if (isUserAdminRole(currentUser)) {
            return;
        }
        //userService.saveUserLastActivity(currentCompany.getUser().getId(), Instant.now());
    }

}
