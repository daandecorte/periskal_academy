package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.dateTime < :dateTime")
    int deleteByDateTimeBefore(LocalDateTime dateTime);
}
