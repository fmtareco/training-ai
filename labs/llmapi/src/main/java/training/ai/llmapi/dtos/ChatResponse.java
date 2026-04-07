package training.ai.llmapi.dtos;


import java.util.List;

public record ChatResponse(List<Choice> choices) {

    public record Choice(Message message) { }

}
