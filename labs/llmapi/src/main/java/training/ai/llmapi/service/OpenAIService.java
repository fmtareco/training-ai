package training.ai.llmapi.service;

import training.ai.llmapi.dtos.ChatRequest;
import training.ai.llmapi.dtos.ChatResponse;
import training.ai.llmapi.dtos.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class OpenAIService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String askLLM(String userInput) {

        // Build request body
        ChatRequest request = new ChatRequest(
            model,
            List.of(
                new Message("system", "You are a helpful assistant"),
                new Message("user", userInput))
        );

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        // Call API
        try {
            ResponseEntity<ChatResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    ChatResponse.class
            );
            return response.getBody()
                    .choices()
                    .get(0)
                    .message()
                    .content();
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

}
