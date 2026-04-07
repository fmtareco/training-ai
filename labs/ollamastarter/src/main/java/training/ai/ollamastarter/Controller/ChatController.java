package training.ai.ollamastarter.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    // ChatClient.Builder is autoconfigured by Spring AI
    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ai/test")
    public String generate(@RequestParam(defaultValue = "Tell me a joke about Java") String message) {
        try {
            return chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Example Controller for Streaming
    @GetMapping(value = "/ai/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam(defaultValue = "message") String message) {
        return chatClient.prompt().user(message).stream().content();
    }
}
