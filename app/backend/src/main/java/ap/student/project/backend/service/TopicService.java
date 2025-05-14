package ap.student.project.backend.service;

import ap.student.project.backend.dao.TopicRepository;
import ap.student.project.backend.dto.TopicDTO;
import ap.student.project.backend.entity.Topic;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class that manages operations related to Topic entities.
 * This service handles creating, retrieving, updating, and deleting Topic records.
 */
@Service
public class TopicService {
    private final TopicRepository topicRepository;

     /**
     * Constructs a new TopicService with the necessary dependencies.
     *
     * @param topicRepository The repository used for Topic entity persistence operations
     */
    public TopicService(TopicRepository topicRepository) { this.topicRepository = topicRepository; }

    /**
     * Retrieves all Topic entities from the database.
     *
     * @return A list containing all Topic entities
     */
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    /**
     * Finds a Topic entity by its ID.
     *
     * @param id The ID of the Topic to find
     * @return The found Topic entity
     * @throws NotFoundException If no Topic with the given ID exists
     */
    public Topic findById(int id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if(topic == null) {
            throw new NotFoundException("Topic with id " + id + " not found");
        }
        return topic;
    }

    /**
     * Creates and saves a new Topic entity from the provided DTO.
     *
     * @param topicDTO The data transfer object containing the Topic information
     * @return The newly created and saved Topic entity
     */
    public Topic save(TopicDTO topicDTO) {
        Topic topic = new Topic();
        topic.setTitle(topicDTO.title());
        return topicRepository.save(topic);
    }

    /**
     * Updates an existing Topic entity with new information from the provided DTO.
     *
     * @param id The ID of the Topic to update
     * @param topicDTO The data transfer object containing the updated Topic information
     * @return The updated Topic entity
     * @throws NotFoundException If no Topic with the given ID exists
     */
    public Topic update(int id, TopicDTO topicDTO) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if(topic == null) {
            throw new NotFoundException("Topic with id " + id + " not found");
        }
        topic.setTitle(topicDTO.title());
        return topicRepository.save(topic);
    }

     /**
     * Deletes a Topic entity by its ID.
     *
     * @param id The ID of the Topic to delete
     * @throws NotFoundException If no Topic with the given ID exists
     */
    public void delete(int id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if(topic == null) {
            throw new NotFoundException("Topic with id " + id + " not found");
        }
        topicRepository.delete(topic);
    }
}
