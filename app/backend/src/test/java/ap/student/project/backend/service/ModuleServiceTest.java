package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleService moduleService;

    private Module module;
    private ModuleDTO moduleDTO;

    @BeforeEach
    void setUp() {
        module = new Module();
        module.setId(1);
        module.setActive(true);
        moduleDTO = new ModuleDTO(null, null, false, null, null, null);
    }

    @Test
    void save_ShouldSaveModule_WhenModuleDoesNotExist() {
        Module module = new Module();
        BeanUtils.copyProperties(moduleDTO, module);

        moduleService.save(moduleDTO);
        verify(moduleRepository, times(1)).save(any(Module.class));
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenModuleNotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            moduleService.findById(1);
        });

        assertEquals("Module with id 1 not found", exception.getMessage());
    }

    @Test
    void findById_ShouldReturnModule_WhenModuleExists() {
        when(moduleRepository.findById(1)).thenReturn(Optional.of(module));

        Module foundModule = moduleService.findById(1);

        assertNotNull(foundModule);
        assertEquals(1, foundModule.getId());
        assertTrue(foundModule.isActive());
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenModuleNotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            moduleService.update(1, moduleDTO);
        });

        assertEquals("Module with id 1 not found", exception.getMessage());
        verify(moduleRepository, never()).save(any(Module.class));
    }

    @Test
    void update_ShouldUpdateModule_WhenModuleExists() {
        when(moduleRepository.findById(1)).thenReturn(Optional.of(module));

        moduleService.update(1, moduleDTO);

        verify(moduleRepository, times(1)).save(module);
        assertFalse(module.isActive());
    }

    @Test
    void findAll_ShouldReturnModuleList() {
        List<Module> modules = Arrays.asList(module, new Module());
        when(moduleRepository.findAll()).thenReturn(modules);

        List<Module> result = moduleService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void delete_ShouldCallDeleteById() {
        moduleService.delete(1);

        verify(moduleRepository, times(1)).deleteById(1);
    }
}
