package ap.student.project.backend.security;

import ap.student.project.backend.entity.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// THis is a placeholder class that will later be refactored into the authentication implementation (dongle or password authentication)

@Service
public class PlaceholderAuthenticationService {

    private final RoleAuthoritiesConverter roleAuthoritiesConverter;

    public PlaceholderAuthenticationService(RoleAuthoritiesConverter roleAuthoritiesConverter) {
        this.roleAuthoritiesConverter = roleAuthoritiesConverter;
    }

    // Creates a UserDetails with the requested role
    public UserDetails authenticateAndGetUserDetails(String identifier, Role role) {
        // This will later call the authentication API and extract role information

        // Placeholder user build for demonstration
        // Spring Security's built-in User class builder pattern
        return User.builder()
                .username(identifier)
                .password("") // No password
                .authorities(roleAuthoritiesConverter.getAuthoritiesFromRole(role))
                .build();
    }
}
