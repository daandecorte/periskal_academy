package ap.student.project.backend.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import ap.student.project.backend.entity.Exam;

public record ExamStartResponseDTO(
    Exam exam,
    @JsonProperty("start_time") LocalDateTime startTime
) {}