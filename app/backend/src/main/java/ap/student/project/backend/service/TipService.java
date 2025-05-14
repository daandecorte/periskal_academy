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

/**
 * Service class for managing tips.
 * Handles creating, retrieving, updating, and deleting tips associated with topics.
 */
@Service
public class TipService {
    private final TipRepository tipRepository;
    private final TopicRepository topicRepository;

    /**
     * Constructs a new TipService with the required repositories.
     *
     * @param tipRepository Repository for Tip entity operations
     * @param topicRepository Repository for Topic entity operations
     */
    @Autowired
    public TipService(TipRepository tipRepository, TopicRepository topicRepository) {
        this.tipRepository = tipRepository;
        this.topicRepository = topicRepository;
    }

     /**
     * Retrieves all tips in the system.
     *
     * @return A list of all Tip entities
     */
    public List<Tip> findAll() { return tipRepository.findAll(); }

    /**
     * Finds a tip by its ID.
     *
     * @param id The ID of the tip to find
     * @return The found Tip entity
     * @throws NotFoundException If no tip with the given ID exists
     */
    public Tip findById(int id) throws NotFoundException {
        Tip tip = tipRepository.findById(id).orElse(null);
        if (tip == null) {
            throw new NotFoundException("Tip with id " + id + " not found");
        }
        return tip;
    }
    
    /**
     * Creates and saves a new tip.
     *
     * @param tipDTO Data transfer object containing tip information
     * @return The saved Tip entity
     * @throws NotFoundException If the topic is not found or the topic_id is invalid
     */
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
