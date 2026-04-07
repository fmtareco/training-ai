package training.ai.llmapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.ai.llmapi.service.OpenAIService;

@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final OpenAIService openAIService;

    public LLMController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello");
    }
    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        String response = openAIService.askLLM(question);
        return ResponseEntity.ok(response);
    }
}
