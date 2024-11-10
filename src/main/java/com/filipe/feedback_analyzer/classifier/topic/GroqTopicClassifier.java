package com.filipe.feedback_analyzer.classifier.topic;

import com.filipe.feedback_analyzer.classifier.Classifier;
import com.filipe.feedback_analyzer.client.GroqClient;
import com.filipe.feedback_analyzer.dto.FeatureCode;
import com.filipe.feedback_analyzer.exceptions.FeedbackClassificationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GroqTopicClassifier implements Classifier {

    private final GroqClient groqClient;

    public GroqTopicClassifier(GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    @Override
    public String classify(String text) {

        String prompt = buildTopicPrompt(text);
        Optional<String> response = groqClient.requestPrompt("llama3-8b-8192", prompt);
        if(response.isPresent()) {
            return response.get();
        }
        else {
            throw new FeedbackClassificationException("Falha ao receber resposta da API para o feedback: " + text, null);
        }

    }

    private String buildTopicPrompt(String text) {
        StringBuilder topicPrompt = new StringBuilder();


        topicPrompt.append("Escolha o número da opção que melhor representa o feedback do usuário a partir da lista abaixo:\n\n");
        int index = 0;
        for (FeatureCode code : FeatureCode.values()) {
            topicPrompt.append(index++).append(" - ").append(code.name()).append("\n");
        }


        topicPrompt.append("\nFeedback: \"").append(text).append("\"\n\n");

        topicPrompt.append("Forneça apenas o número da opção correspondente e uma razão concisa (máximo 2 frases). O formato deve ser número;razão, Exemplo: 0; razão da escolha.");

        return topicPrompt.toString();
    }
}
