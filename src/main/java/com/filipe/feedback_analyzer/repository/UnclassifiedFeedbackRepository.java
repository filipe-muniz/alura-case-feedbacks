package com.filipe.feedback_analyzer.repository;

import com.filipe.feedback_analyzer.entity.UnclassifiedFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnclassifiedFeedbackRepository extends JpaRepository<UnclassifiedFeedback, Long> {
}
