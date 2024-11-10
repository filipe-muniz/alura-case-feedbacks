package com.filipe.feedback_analyzer.client;

import com.filipe.feedback_analyzer.dto.GroqRequest;
import com.filipe.feedback_analyzer.dto.GroqResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class GroqClient {
    @Value("${groq.api.url}")
    private String apiURL;

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GroqClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<String> requestPrompt(String model, String text) {
        try {
            GroqRequest req = new GroqRequest(model, text);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            HttpEntity<GroqRequest> entity = new HttpEntity<>(req, headers);

            ResponseEntity<GroqResponse> res = restTemplate.exchange(
                    apiURL,
                    HttpMethod.POST,
                    entity,
                    GroqResponse.class
            );

            GroqResponse responseBody = res.getBody();
            if (responseBody != null && responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
                String content = responseBody.getChoices().get(0).getMessage().getContent();
                return Optional.ofNullable(content);
            } else {
                System.err.println("Resposta do Groq não contém dados válidos.");
                return Optional.empty();
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Erro de cliente/servidor na API Groq: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao analisar o texto: " + e.getMessage());
        }
        return Optional.empty();
    }
}
