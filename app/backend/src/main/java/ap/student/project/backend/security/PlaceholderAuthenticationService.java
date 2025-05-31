package ap.student.project.backend.security;

import ap.student.project.backend.entity.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PlaceholderAuthenticationService {

    private final RoleAuthoritiesConverter roleAuthoritiesConverter;

    public PlaceholderAuthenticationService(RoleAuthoritiesConverter roleAuthoritiesConverter) {
        this.roleAuthoritiesConverter = roleAuthoritiesConverter;
    }

    // Creates a UserDetails with the requested role
    public UserDetails authenticateAndGetUserDetails(String identifier, Role role) {

        // Spring Security's built-in User class builder pattern
        return User.builder()
                .username(identifier)
                .password("") // No password
                .authorities(roleAuthoritiesConverter.getAuthoritiesFromRole(role))
                .build();
    }
}
