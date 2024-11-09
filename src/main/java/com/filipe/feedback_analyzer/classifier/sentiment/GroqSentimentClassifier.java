package com.filipe.feedback_analyzer.classifier.sentiment;

import com.filipe.feedback_analyzer.dto.GroqRequest;
import com.filipe.feedback_analyzer.dto.GroqResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Component
public class GroqSentimentClassifier implements SentimementClassifier{
    @Value("${groq.api.url}")
    private String apiURL;
    @Value("${groq.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;
    public GroqSentimentClassifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public String classifySentiment(String text) {
        try{
            GroqRequest req = new GroqRequest("llama3-8b-8192", text);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            HttpEntity<GroqRequest> entity = new HttpEntity<>(req, headers);
            ResponseEntity<GroqResponse> res= restTemplate.exchange(
                    apiURL,
                    HttpMethod.POST,
                    entity,
                    GroqResponse.class
            );
            return res.getBody().getChoices().get(0).getMessage().getContent();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Erro ao analisar o sentimento.";
        }

    }
}
