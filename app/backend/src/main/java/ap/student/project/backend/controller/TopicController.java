package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TopicDTO;
import ap.student.project.backend.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling topic-related HTTP requests.
 * Manages operations for topics including creation, retrieval, updates, and deletion.
 */
@CrossOrigin
@RestController
public class TopicController {
    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicRepository) {
        this.topicService = topicRepository;
    }

    /**
     * Retrieves all topics from the system.
     *
     * @return ResponseEntity containing a list of all topics with HTTP status 200 (OK)
     */
    @GetMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(topicService.findAll());
    }

    /**
     * Retrieves a specific topic by its ID.
     *
     * @param id The ID of the topic to retrieve
     * @return ResponseEntity containing the topic with HTTP status 200 (OK)
     */
    @GetMapping(value = "/topics/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.findById(id));
    }

    /**
     * Creates a new topic in the system.
     *
     * @param topic The topic data transfer object containing topic information
     * @return ResponseEntity containing the created topic with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/topics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody TopicDTO topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.save(topic));
    }

    /**
     * Updates a specific topic with new information.
     *
     * @param id    The ID of the topic to update
     * @param topic The topic data transfer object containing updated topic information
     * @return ResponseEntity containing the updated topic with HTTP status 200 (OK)
     */
    @PutMapping(value = "/topics/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable int id, @RequestBody TopicDTO topic) {
        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.update(id, topic));
    }

    /**
     * Deletes a specific topic from the system.
     *
     * @param id The ID of the topic to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT) and a deletion confirmation message
     */
    @DeleteMapping(value = "/topics/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable int id) {
        this.topicService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted topic with id " + id);
    }
}
