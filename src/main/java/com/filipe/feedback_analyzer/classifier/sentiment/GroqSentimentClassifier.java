package com.filipe.feedback_analyzer.classifier.sentiment;
import com.filipe.feedback_analyzer.classifier.Classifier;
import com.filipe.feedback_analyzer.client.GroqClient;
import org.springframework.stereotype.Component;



@Component
public class GroqSentimentClassifier implements Classifier {
    private final GroqClient groqClient;

    public GroqSentimentClassifier(GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    @Override
    public String classify(String text) {
       String prompt =  String.format(
                "Classifique o seguinte feedback em uma das três categorias: POSITIVO, NEGATIVO ou INCONCLUSIVO, baseando-se no tom e conteúdo da mensagem.\n\nFeedback: \"%s\"\n\nPor favor, forneça apenas a classificação final seguindo o exemplo:",
               text);
        return groqClient.requestPrompt("llama3-8b-8192", prompt);
    }
}
