package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.exceptions.DuplicateException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public void save(ModuleDTO moduleDTO) {
        Module module = new Module();
        BeanUtils.copyProperties(moduleDTO, module);
        if(moduleRepository.existsById(module.getId())) {
            throw new DuplicateException("module with id " + module.getId() + " already exists");
        }
        moduleRepository.save(module);
    }
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }
}
