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
        String prompt = String.format(
                "Contexto: A AluMind é uma startup que oferece um aplicativo focado em bem-estar e saúde mental, proporcionando aos usuários acesso a meditações guiadas, sessões de terapia e conteúdos educativos sobre saúde mental. Com o crescimento acelerado da base de usuários, surgiram desafios para analisar os feedbacks recebidos de várias plataformas (como canais de atendimento ao cliente, comunidades no Discord e redes sociais). A tarefa é auxiliar na classificação desses feedbacks como SPAM ou não.\n\n" +
                        "Agora, verifique se o seguinte feedback é um SPAM.\n" +
                        "Feedback: \"%s\"\n\n" +
                        "Por favor, forneça apenas \"SIM\" se for SPAM ou \"NAO\" se não for:",
                text
        );
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
