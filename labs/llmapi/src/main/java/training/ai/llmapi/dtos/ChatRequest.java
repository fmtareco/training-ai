package training.ai.llmapi.dtos;

import java.util.List;

public record ChatRequest(String model, List<Message> messages) {
}
