package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.service.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ModuleController {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping("/modules")
    public List<Module> getModules() {
        return this.moduleService.findAll();
    }
    @PostMapping("/modules")
    public Module addModule(@RequestBody ModuleDTO moduleDTO) {
        Module module = new Module();
        BeanUtils.copyProperties(moduleDTO, module);
        this.moduleService.save(moduleDTO);
        return module;
    }
}
