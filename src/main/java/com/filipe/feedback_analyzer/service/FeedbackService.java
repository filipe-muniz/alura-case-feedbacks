package com.filipe.feedback_analyzer.service;

import com.filipe.feedback_analyzer.classifier.Classifier;
import com.filipe.feedback_analyzer.classifier.sentiment.GroqSentimentClassifier;
import com.filipe.feedback_analyzer.classifier.topic.GroqTopicClassifier;
import com.filipe.feedback_analyzer.dto.FeatureCode;
import com.filipe.feedback_analyzer.dto.FeedbackResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final Classifier sentimementClassifier;
    private final Classifier topicClassifier;

    public FeedbackService(GroqSentimentClassifier sentimementClassifier, GroqTopicClassifier topicClassifier) {
        this.sentimementClassifier = sentimementClassifier;
        this.topicClassifier = topicClassifier;
    }

    public FeedbackResponseDTO classifyFeedback(String feedback) {

        String sentiment = sentimementClassifier.classify(feedback);
        String topicClassification = topicClassifier.classify(feedback);
        String[] parts = topicClassification.split(";", 2);
        String code = parts[0].trim();
        String reason = parts[1].trim();

        FeedbackResponseDTO.RequestedFeature requestedFeature = new FeedbackResponseDTO.RequestedFeature(FeatureCode.values()[Integer.parseInt(code)].name(), reason);

        return new FeedbackResponseDTO(1, sentiment, requestedFeature);

    }

}
