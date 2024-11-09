package com.filipe.feedback_analyzer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackResponseDTO {
    private int id;
    private String sentiment;
    private RequestedFeature requestedFeature;
    public FeedbackResponseDTO(int id, String sentiment, RequestedFeature requestedFeatures) {
        this.id = id;
        this.sentiment = sentiment;
        this.requestedFeature = requestedFeatures;
    }
    @Getter
    @Setter
    public static class RequestedFeature {
        private String code;
        private String reason;

        public RequestedFeature(String code, String reason) {
            this.code = code;
            this.reason = reason;
        }
    }

}
