package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TipDTO;
import ap.student.project.backend.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling tip-related HTTP requests.
 * Manages operations for tips including creation, retrieval, updates, and deletion.
 */
@CrossOrigin
@RestController
public class TipController {
    private final TipService tipService;

    @Autowired
    public TipController(TipService tipService) {
        this.tipService = tipService;
    }

    /**
     * Retrieves all tips from the system.
     *
     * @return ResponseEntity containing a list of all tips with HTTP status 200 (OK)
     */
    @GetMapping(value = "/tips", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(tipService.findAll());
    }

    /**
     * Retrieves a specific tip by its ID.
     *
     * @param id The ID of the tip to retrieve
     * @return ResponseEntity containing the tip with HTTP status 200 (OK)
     */
    @GetMapping(value = "/tips/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTipById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(tipService.findById(id));
    }

    /**
     * Creates a new tip in the system.
     *
     * @param tip The tip data transfer object containing tip information
     * @return ResponseEntity containing the created tip with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/tips", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody TipDTO tip) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.tipService.save(tip));
    }

    /**
     * Updates a specific tip with new information.
     *
     * @param id     The ID of the tip to update
     * @param tipDTO The tip data transfer object containing updated tip information
     * @return ResponseEntity containing the updated tip with HTTP status 200 (OK)
     */
    @PutMapping(value = "/tips/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("id") int id, @RequestBody TipDTO tipDTO) {
        this.tipService.update(id, tipDTO);
        return ResponseEntity.ok(tipDTO);
    }

    /**
     * Deletes a specific tip from the system.
     *
     * @param id The ID of the tip to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping(value = "/tips/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        this.tipService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
