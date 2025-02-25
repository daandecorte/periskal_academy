package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Video;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends ListCrudRepository<Video, Integer> {
}
