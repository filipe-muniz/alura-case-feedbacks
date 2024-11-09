package com.filipe.feedback_analyzer.service;

import com.filipe.feedback_analyzer.classifier.sentiment.SentimementClassifier;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final SentimementClassifier sentimementClassifier;
    public FeedbackService(SentimementClassifier sentimementClassifier) {
        this.sentimementClassifier = sentimementClassifier;
    }
    public String classifyFeedback(String feedback) {
        String prompt = String.format(
                "Classifique o seguinte feedback em uma das três categorias: POSITIVO, NEGATIVO ou INCONCLUSIVO, baseando-se no tom e conteúdo da mensagem.\n\nFeedback: \"%s\"\n\nPor favor, forneça apenas a classificação final:",
                feedback);
        return sentimementClassifier.classifySentiment(prompt);
    }

}
