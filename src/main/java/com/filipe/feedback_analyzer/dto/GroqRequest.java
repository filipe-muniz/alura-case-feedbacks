package com.filipe.feedback_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class GroqRequest {
    private String model;
    private List<Message> messages;
    public GroqRequest(String model, String content) {
        this.model = model;
        this.messages = List.of(new Message("user", content));
        };
    @Getter
    @Setter
    public static class Message{
        private String role;
        private String content;
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
