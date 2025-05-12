package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TopicDTO;
import ap.student.project.backend.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class TopicController {
    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicRepository) {
        this.topicService = topicRepository;
    }

    @GetMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(topicService.findAll());
    }

    @GetMapping(value = "/topics/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.findById(id));
    }

    @PostMapping(value = "/topics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody TopicDTO topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.save(topic));
    }

    @PutMapping(value = "/topics/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable int id, @RequestBody TopicDTO topic) {
        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.update(id, topic));
    }

    @DeleteMapping(value = "/topics/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable int id) {
        this.topicService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted topic with id " + id);
    }
}
