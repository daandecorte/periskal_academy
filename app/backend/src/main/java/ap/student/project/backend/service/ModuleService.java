package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
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
        moduleRepository.save(module);
    }
    public Module findById(int id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if(module == null) {
            throw new NotFoundException("module with id " + id + " not found");
        }
        return module;
    }
    public void update(int id, ModuleDTO moduleDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if(module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        BeanUtils.copyProperties(moduleDTO, module);
        moduleRepository.save(module);
    }
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }
    public void delete(int id) {
        moduleRepository.deleteById(id);
    }
}
