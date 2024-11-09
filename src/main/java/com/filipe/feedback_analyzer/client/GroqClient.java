package com.filipe.feedback_analyzer.client;

import com.filipe.feedback_analyzer.dto.GroqRequest;
import com.filipe.feedback_analyzer.dto.GroqResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    public String requestPrompt(String model, String text) {
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

            return res.getBody().getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao analisar o texto.";
        }
    }
}
