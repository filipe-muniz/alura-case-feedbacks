package com.filipe.feedback_analyzer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String feedback;
    private String sentiment;
    private String featureCode;
    private String reason;
    public Feedback(String feedbackText, String sentiment, String featureCode, String reason) {
        this.feedback = feedbackText;
        this.sentiment = sentiment;
        this.featureCode = featureCode;
        this.reason = reason;
    }
}
