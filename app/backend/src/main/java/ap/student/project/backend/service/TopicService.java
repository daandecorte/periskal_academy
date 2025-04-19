package ap.student.project.backend.service;

import ap.student.project.backend.dao.TopicRepository;
import ap.student.project.backend.dto.TopicDTO;
import ap.student.project.backend.entity.Topic;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) { this.topicRepository = topicRepository; }

    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    public Topic findById(int id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if(topic == null) {
            throw new NotFoundException("Topic with id " + id + " not found");
        }
        return topic;
    }

    public Topic save(TopicDTO topicDTO) {
        Topic topic = new Topic();
        topic.setTitle(topicDTO.title());
        return topicRepository.save(topic);
    }

    public Topic update(int id, TopicDTO topicDTO) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if(topic == null) {
            throw new NotFoundException("Topic with id " + id + " not found");
        }
        topic.setTitle(topicDTO.title());
        return topicRepository.save(topic);
    }

    public void delete(int id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if(topic == null) {
            throw new NotFoundException("Topic with id " + id + " not found");
        }
        topicRepository.delete(topic);
    }
}
