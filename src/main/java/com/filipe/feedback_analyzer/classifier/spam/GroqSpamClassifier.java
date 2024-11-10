package com.filipe.feedback_analyzer.classifier.spam;

import com.filipe.feedback_analyzer.classifier.Classifier;
import com.filipe.feedback_analyzer.client.GroqClient;
import com.filipe.feedback_analyzer.exceptions.FeedbackClassificationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class GroqSpamClassifier implements Classifier {
    private final GroqClient groqClient;
    private static final Set<String> VALID_RESPONSES = Set.of("SIM", "NAO");

    public GroqSpamClassifier(final GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    @Override
    public String classify(String text) {
        String prompt = String.format("Verifique se o seguinte feedback é um SPAM. " +
                        "\n\nFeedback: \"%s\"\n\nPor favor, forneça apenas \"SIM\" se for SPAM ou \"NÃO\" se não for:",
                text);
        Optional<String> response = groqClient.requestPrompt("llama3-8b-8192", prompt);
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
