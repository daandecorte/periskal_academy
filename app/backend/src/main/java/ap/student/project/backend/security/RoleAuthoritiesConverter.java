package ap.student.project.backend.security;

import ap.student.project.backend.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class RoleAuthoritiesConverter {

    /**
     * Converts a Role enum to to a Spring Security authority
     * To be expanded later with permission logic
     */
    public Collection<? extends GrantedAuthority> getAuthoritiesFromRole(Role role) {
        if (role == null) {
            return Collections.emptyList();
        }

        // The standard naming convention is to prefix roles with "ROLE_"
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}