package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TopicDTO;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@CrossOrigin
@RestController
public class TopicController {
    private final TopicService topicService;
    private final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    public TopicController(TopicService topicRepository) { this.topicService = topicRepository; }

    @GetMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() { return ResponseEntity.ok(topicService.findAll()); }

    @GetMapping(value = "/topics/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(topicService.findById(id));
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(value = "/topics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody TopicDTO topic) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(topicService.save(topic));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping(value = "/topics/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable int id, @RequestBody TopicDTO topic) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(this.topicService.update(id, topic));
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/topics/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable int id) {
        try{
            this.topicService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted topic with id " + id);
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
