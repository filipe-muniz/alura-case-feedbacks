package com.filipe.feedback_analyzer.service;


import com.filipe.feedback_analyzer.classifier.sentiment.GroqSentimentClassifier;
import com.filipe.feedback_analyzer.classifier.topic.GroqTopicClassifier;
import com.filipe.feedback_analyzer.classifier.spam.GroqSpamClassifier; // Importando o classificador de SPAM
import com.filipe.feedback_analyzer.dto.FeatureCode;
import com.filipe.feedback_analyzer.dto.FeedbackResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final GroqSpamClassifier spamClassifier; // Adicionando o classificador de SPAM
    private final GroqSentimentClassifier sentimementClassifier;
    private final GroqTopicClassifier topicClassifier;

    public FeedbackService(GroqSpamClassifier spamClassifier, GroqSentimentClassifier sentimementClassifier, GroqTopicClassifier topicClassifier) {
        this.spamClassifier = spamClassifier; // Inicializando o classificador de SPAM
        this.sentimementClassifier = sentimementClassifier;
        this.topicClassifier = topicClassifier;
    }

    public FeedbackResponseDTO classifyFeedback(String feedback) {

        String isSpam = spamClassifier.classify(feedback);
        if (isSpam.equalsIgnoreCase("SIM")) {
            return new FeedbackResponseDTO(0, "SPAM", null);
        }

        String sentiment = sentimementClassifier.classify(feedback);
        String topicClassification = topicClassifier.classify(feedback);
        String[] parts = topicClassification.split(";", 2);
        String code = parts[0].trim();
        String reason = parts[1].trim();

        FeedbackResponseDTO.RequestedFeature requestedFeature = new FeedbackResponseDTO.RequestedFeature(
                FeatureCode.values()[Integer.parseInt(code)].name(), reason);

        return new FeedbackResponseDTO(1, sentiment, requestedFeature);
    }
}
