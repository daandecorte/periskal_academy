package ap.student.project.backend.service;

import ap.student.project.backend.dao.TipRepository;
import ap.student.project.backend.dao.TopicRepository;
import ap.student.project.backend.dto.TipDTO;
import ap.student.project.backend.entity.Tip;
import ap.student.project.backend.entity.Topic;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipService {
    private final TipRepository tipRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public TipService(TipRepository tipRepository, TopicRepository topicRepository) {
        this.tipRepository = tipRepository;
        this.topicRepository = topicRepository;
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
        Topic topic;
        if(tipDTO.topic_id() >= 0){
            topic = topicRepository.findById(tipDTO.topic_id()).orElse(null);
            if(topic == null){
                throw new NotFoundException("Topic with id " + tipDTO.topic_id() + " not found");
            }
        } else {
            throw new NotFoundException("Topic id can't be lower than 0");
        }
        Tip tip = new Tip(topic, tipDTO.text());
        return tipRepository.save(tip);
    }

    public void update(int id, TipDTO tipDTO) throws NotFoundException {
        Tip updatedTip = tipRepository.findById(id).orElse(null);
        if (updatedTip == null) {
            throw new NotFoundException("Tip with id " + id + " not found");
        }
        updatedTip.setText(tipDTO.text());
        tipRepository.save(updatedTip);
    }

    public void deleteById(int id) {
        Tip tip = tipRepository.findById(id).orElse(null);
        if (tip == null) {
            throw new NotFoundException("Tip with id " + id + " not found");
        }
        tipRepository.deleteById(id);
    }
}
