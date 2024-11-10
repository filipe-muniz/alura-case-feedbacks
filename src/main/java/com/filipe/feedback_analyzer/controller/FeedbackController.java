package com.filipe.feedback_analyzer.controller;

import com.filipe.feedback_analyzer.dto.FeedbackResponseDTO;
import com.filipe.feedback_analyzer.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<FeedbackResponseDTO> classifyFeedback(@RequestBody String feedback) {
        var response = feedbackService.classifyAndSaveFeedback(feedback);
        if(response.getId() == 0){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
