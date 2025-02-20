package ap.student.project.backend.controller;

import ap.student.project.backend.entity.TestEntity;
import ap.student.project.backend.repository.TestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final TestRepository repository;

    public TestController(TestRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TestEntity> getAllTests() {
        return repository.findAll();
    }
}
