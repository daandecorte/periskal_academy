package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleProgressRepository;
import ap.student.project.backend.dto.ModuleProgressDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.ModuleProgress;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleProgressService {
    private final ModuleProgressRepository moduleProgressRepository;
    private final UserModuleService userModuleService;

    public ModuleProgressService(ModuleProgressRepository moduleProgressRepository, UserModuleService userModuleService) {
        this.moduleProgressRepository = moduleProgressRepository;
        this.userModuleService = userModuleService;
    }

    public void save(ModuleProgressDTO moduleProgressDTO) {
        ModuleProgress moduleProgress = new ModuleProgress();
        UserModule userModule = userModuleService.findById(moduleProgressDTO.userModuleId());
        moduleProgress.setUserModule(userModule);
        BeanUtils.copyProperties(moduleProgressDTO, moduleProgress);
        moduleProgressRepository.save(moduleProgress);
    }
    public void update(int id, ModuleProgressDTO moduleProgressDTO) throws NotFoundException {
        ModuleProgress moduleProgress = moduleProgressRepository.findById(id).orElse(null);
        if (moduleProgress == null) {
            throw new NotFoundException("Module progress with id " + id + " not found");
        }
        BeanUtils.copyProperties(moduleProgressDTO, moduleProgress);
        moduleProgressRepository.save(moduleProgress);
    }

    public List<ModuleProgress> findAll() {
        return moduleProgressRepository.findAll();
    }
}
