package ap.student.project.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class RoleTestController {
    @GetMapping("/skipper")
    @PreAuthorize("hasRole('SKIPPER')")
    public String skipperEndpoint() {
        return "You have SKIPPER role access";
    }

    @GetMapping("/installer")
    @PreAuthorize("hasRole('INSTALLER')")
    public String installerEndpoint() {
        return "You have INSTALLER role access";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "You have ADMIN role access";
    }

    @GetMapping("/support")
    @PreAuthorize("hasRole('SUPPORT')")
    public String supportEndpoint() {
        return "You have SUPPORT role access";
    }

    @GetMapping("/fleetmanager")
    @PreAuthorize("hasRole('FLEETMANAGER')")
    public String fleetManagerEndpoint() {
        return "You have FLEETMANAGER role access";
    }
}
