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
        String prompt = String.format("A AluMind é uma startup de bem-estar e saúde mental, oferecendo meditações guiadas, terapia, e conteúdos educativos para os usuários. O feedback abaixo foi enviado por um usuário e precisa ser classificado para identificar se é SPAM. Um feedback é considerado SPAM se for ofensivo, irrelevante, ou não relacionado aos serviços da AluMind.\n\nFeedback: \"%s\"\n\nPor favor, responda com 'SIM' se o feedback for SPAM (ou seja, ofensivo, irrelevante ou sem relação com a AluMind) ou 'NAO' se o feedback for relevante e construtivo:",
                text);
        Optional<String> response = groqClient.requestPrompt("llama3-70b-8192", prompt);
        if (response.isPresent()) {
            return response.get();
        } else {
            throw new FeedbackClassificationException("Falha ao receber resposta da API para o feedback: " + text, null);
        }
    }
}
