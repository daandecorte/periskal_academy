package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TipDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.TipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class TipController {
    private final TipService tipService;

    @Autowired
    public TipController(TipService tipService) { this.tipService = tipService; }

    @GetMapping(value = "/tips", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(tipService.findAll());
    }

    @GetMapping(value = "/tips/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTipById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(tipService.findById(id));
    }

    @PostMapping(value = "/tips", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody TipDTO tip) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.tipService.save(tip));
    }

    @PutMapping(value = "/tips/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("id") int id, @RequestBody TipDTO tipDTO) {
        this.tipService.update(id, tipDTO);
        return ResponseEntity.ok(tipDTO);
    }

    @DeleteMapping(value = "/tips/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        this.tipService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
