package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ChatMember;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface ChatMemberRepository extends ListCrudRepository<ChatMember, Integer> {
}
