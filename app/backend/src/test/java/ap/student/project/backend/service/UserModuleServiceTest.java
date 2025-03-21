package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserModuleRepository;
import ap.student.project.backend.dto.UserModuleDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserModuleServiceTest {

    private UserModuleRepository userModuleRepository;
    private UserService userService;
    private ModuleService moduleService;
    private UserModuleService userModuleService;

    @BeforeEach
    void setUp() {
        userModuleRepository = mock(UserModuleRepository.class);
        userService = mock(UserService.class);
        moduleService = mock(ModuleService.class);
        userModuleService = new UserModuleService(userModuleRepository, userService, moduleService);
    }

    @Test
    void save_shouldSaveUserModule() {
        User user = new User();
        user.setId(1);
        Module module = new Module();
        module.setId(2);
        UserModuleDTO dto = new UserModuleDTO(null, 2, 1);

        when(userService.findById(1)).thenReturn(user);
        when(moduleService.findById(2)).thenReturn(module);

        userModuleService.save(dto);

        ArgumentCaptor<UserModule> captor = ArgumentCaptor.forClass(UserModule.class);
        verify(userModuleRepository, times(1)).save(captor.capture());
        UserModule savedUserModule = captor.getValue();

        assertEquals(1, savedUserModule.getUser().getId());
        assertEquals(2, savedUserModule.getModule().getId());
    }

    @Test
    void findAll_shouldReturnAllUserModules() {
        UserModule um1 = new UserModule();
        UserModule um2 = new UserModule();
        when(userModuleRepository.findAll()).thenReturn(Arrays.asList(um1, um2));

        List<UserModule> result = userModuleService.findAll();

        assertEquals(2, result.size());
        verify(userModuleRepository, times(1)).findAll();
    }
}
