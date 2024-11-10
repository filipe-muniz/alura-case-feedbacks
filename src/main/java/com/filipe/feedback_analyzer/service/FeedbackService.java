package com.filipe.feedback_analyzer.service;

import com.filipe.feedback_analyzer.classifier.sentiment.GroqSentimentClassifier;
import com.filipe.feedback_analyzer.classifier.topic.GroqTopicClassifier;
import com.filipe.feedback_analyzer.classifier.spam.GroqSpamClassifier;
import com.filipe.feedback_analyzer.dto.FeatureCode;
import com.filipe.feedback_analyzer.dto.FeedbackResponseDTO;
import com.filipe.feedback_analyzer.entity.Feedback;
import com.filipe.feedback_analyzer.entity.UnclassifiedFeedback;
import com.filipe.feedback_analyzer.repository.FeedbackRepository;
import com.filipe.feedback_analyzer.repository.UnclassifiedFeedbackRepository;
import com.filipe.feedback_analyzer.exceptions.FeedbackClassificationException;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final GroqSpamClassifier spamClassifier;
    private final GroqSentimentClassifier sentimentClassifier;
    private final GroqTopicClassifier topicClassifier;
    private final FeedbackRepository feedbackRepository;
    private final UnclassifiedFeedbackRepository unclassifiedFeedbackRepository;

    public FeedbackService(GroqSpamClassifier spamClassifier,
                           GroqSentimentClassifier sentimentClassifier,
                           GroqTopicClassifier topicClassifier,
                           FeedbackRepository feedbackRepository,
                           UnclassifiedFeedbackRepository unclassifiedFeedbackRepository) {
        this.spamClassifier = spamClassifier;
        this.sentimentClassifier = sentimentClassifier;
        this.topicClassifier = topicClassifier;
        this.feedbackRepository = feedbackRepository;
        this.unclassifiedFeedbackRepository = unclassifiedFeedbackRepository;
    }

    public FeedbackResponseDTO classifyAndSaveFeedback(String feedbackText) {
        if (isSpam(feedbackText)) {
            return new FeedbackResponseDTO(0, "SPAM", null);
        }

        try {
            String sentiment = classifySentiment(feedbackText);
            FeedbackResponseDTO.RequestedFeature requestedFeature = classifyTopic(feedbackText);

            Feedback feedback = new Feedback(feedbackText, sentiment, requestedFeature.getCode(), requestedFeature.getReason());
            feedbackRepository.save(feedback);

            return new FeedbackResponseDTO(1, sentiment, requestedFeature);

        } catch (FeedbackClassificationException e) {

            UnclassifiedFeedback unclassifiedFeedback = new UnclassifiedFeedback(
                    null,
                    feedbackText,
                    e.getMessage(),
                    java.time.LocalDateTime.now()
            );
            unclassifiedFeedbackRepository.save(unclassifiedFeedback);
            return new FeedbackResponseDTO(0, "ERRO", null);
        }
    }

    private boolean isSpam(String feedbackText) {
        String isSpam = spamClassifier.classify(feedbackText);
        return isSpam.equalsIgnoreCase("SIM");
    }

    private String classifySentiment(String feedbackText) {
        return sentimentClassifier.classify(feedbackText);
    }

    private FeedbackResponseDTO.RequestedFeature classifyTopic(String feedbackText) {
        String topicClassification = topicClassifier.classify(feedbackText);
        String[] parts = topicClassification.split(";", 2);
        String code = parts[0].trim();
        String reason = parts[1].trim();
        return new FeedbackResponseDTO.RequestedFeature(FeatureCode.values()[Integer.parseInt(code)].name(), reason);
    }
}
