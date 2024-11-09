package com.filipe.feedback_analyzer.controller;

import com.filipe.feedback_analyzer.dto.FeedbackResponseDTO;
import com.filipe.feedback_analyzer.service.FeedbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
    @PostMapping("/classify")
    public FeedbackResponseDTO classifyFeedback(@RequestBody String feedback) {
        return feedbackService.classifyFeedback(feedback);
    }
}
