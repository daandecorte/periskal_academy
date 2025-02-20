package ap.student.project.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ap.student.project.backend.dao.TestRepository;

@SpringBootApplication
public class BackendApplication{

	private final TestRepository repo;

	public BackendApplication(TestRepository repo) {
		this.repo=repo;
	}
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

	}
}
