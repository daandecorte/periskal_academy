package ap.student.project.backend.service;

import ap.student.project.backend.dao.TipRepository;
import ap.student.project.backend.dto.TipDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Tip;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipService {
    private final TipRepository tipRepository;
    private final ModuleService moduleService;

    @Autowired
    public TipService(TipRepository tipRepository, ModuleService moduleService) {
        this.tipRepository = tipRepository;
        this.moduleService = moduleService;
    }

    public List<Tip> findAll() { return tipRepository.findAll(); }

    public Tip findById(int id) throws NotFoundException {
        Tip tip = tipRepository.findById(id).orElse(null);
        if (tip == null) {
            throw new NotFoundException("Tip with id " + id + " not found");
        }
        return tip;
    }

    public Tip save(TipDTO tipDTO){
        Module module = moduleService.findById((tipDTO.moduleId()));
        Tip tip = new Tip(tipDTO.text(), module);
        return tipRepository.save(tip);
    }

    public void update(int id, TipDTO tipDTO) throws NotFoundException {
        Tip updatedTip = tipRepository.findById(id).orElse(null);
        if (updatedTip == null) {
            throw new NotFoundException("Tip with id " + id + " not found");
        }
        updatedTip.setText(tipDTO.text());
        try{
            updatedTip.setModule(moduleService.findById(tipDTO.moduleId()));
        } catch (NotFoundException e) {
            updatedTip.setModule(null);
        }
        tipRepository.save(updatedTip);
    }

    public void deleteById(int id) {
        tipRepository.deleteById(id);
    }
}
