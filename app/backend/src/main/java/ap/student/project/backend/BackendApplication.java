package ap.student.project.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ap.student.project.backend.entity.TestEntity;
import ap.student.project.backend.repository.TestRepository;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner{

	private final TestRepository repo;

	public BackendApplication(TestRepository repo) {
		this.repo=repo;
	}
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

	}

}
