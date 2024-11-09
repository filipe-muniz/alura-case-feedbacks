package com.filipe.feedback_analyzer.classifier.spam;

import com.filipe.feedback_analyzer.classifier.Classifier;
import com.filipe.feedback_analyzer.client.GroqClient;
import org.springframework.stereotype.Component;

@Component
public class GroqSpamClassifier implements Classifier {
    private final GroqClient groqClient;
    public GroqSpamClassifier(final GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    @Override
    public String classify(String text) {
        String prompt = String.format("Verifique se o seguinte feedback é um SPAM. " +
                        "\n\nFeedback: \"%s\"\n\nPor favor, forneça apenas \"SIM\" se for SPAM ou \"NÃO\" se não for:",
                text);
        return groqClient.requestPrompt("llama3-8b-8192", prompt);
    }
}
