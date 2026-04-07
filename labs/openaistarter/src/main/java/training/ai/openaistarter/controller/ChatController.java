package training.ai.openaistarter.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ai/test")
    public String completion(@RequestParam(defaultValue = "Olá, como estás?") String message) {
        try {
            return chatClient.prompt().user(message).call().content();
        } catch (Exception e) {
            return "Erro na API: " + e.getMessage(); // Provavelmente dirá "insufficient_quota"
        }
    }
}
