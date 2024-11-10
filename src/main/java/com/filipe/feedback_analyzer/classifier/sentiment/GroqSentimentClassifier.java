package com.filipe.feedback_analyzer.classifier.sentiment;

import com.filipe.feedback_analyzer.classifier.Classifier;
import com.filipe.feedback_analyzer.client.GroqClient;
import com.filipe.feedback_analyzer.exceptions.FeedbackClassificationException;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Set;

@Component
public class GroqSentimentClassifier implements Classifier {
    private final GroqClient groqClient;
    private static final Set<String> VALID_RESPONSES = Set.of("POSITIVO", "NEGATIVO", "INCONCLUSIVO");

    public GroqSentimentClassifier(GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    @Override
    public String classify(String text) {
        String prompt =  String.format(
                "Classifique o seguinte feedback em uma das três categorias: POSITIVO, NEGATIVO ou INCONCLUSIVO, baseando-se no tom e conteúdo da mensagem.\n\nFeedback: \"%s\"\n\nPor favor, forneça apenas a classificação final seguindo o exemplo:",
                text);
        Optional<String> response = groqClient.requestPrompt("llama3-70b-8192", prompt);

        if (response.isPresent()) {
            String classification = response.get().trim().toUpperCase();
            if (VALID_RESPONSES.contains(classification)) {
                return classification;
            } else {
                throw new FeedbackClassificationException("Classificação inválida recebida: " + classification, null);
            }
        } else {
            throw new FeedbackClassificationException("Falha ao receber resposta da API para o feedback: " + text, null);
        }
    }
}
