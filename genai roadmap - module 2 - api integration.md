# **GenAI roadmap** - Module 2 — First API Integration (Java)


* Create Spring Boot app
* Call LLM API (REST)

Example:

```java
RestTemplate → POST to OpenAI API
```

### Build

* `/summarize` endpoint

### Goal

LLM integrated into backend


Below is a **clean, minimal Spring Boot application** that calls an LLM API via REST using `RestTemplate`. This is **production-structured enough for interviews**, but still simple to run.

I’ll show:

1. Project setup
2. Configuration (API key, URL)
3. DTOs (request/response)
4. Service (LLM call)
5. Controller (endpoint)
6. Example request

---

# 🧱 1️⃣ Project Setup (Maven)

## `pom.xml`

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Lombok (optional but useful) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- JSON -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```

---

# ⚙️ 2️⃣ Configuration

## `application.yml`

```yaml
openai:
  api-key: YOUR_API_KEY
  url: https://api.openai.com/v1/chat/completions
  model: gpt-4o-mini
```

---

# 🧩 3️⃣ DTOs

## Request DTO

```java
public class ChatRequest {
    private String model;
    private List<Message> messages;

    // getters/setters
}
```

---

## Message DTO

```java
public class Message {
    private String role;
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // getters/setters
}
```

---

## Response DTO (partial, simplified)

```java
public class ChatResponse {
    private List<Choice> choices;

    public static class Choice {
        private Message message;
        // getters/setters
    }

    // getters/setters
}
```

---

# 🔌 4️⃣ RestTemplate Configuration

```java
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

# 🧠 5️⃣ LLM Service (Core Logic)

```java
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
        ChatRequest request = new ChatRequest();
        request.setModel(model);
        request.setMessages(List.of(
                new Message("system", "You are a helpful assistant"),
                new Message("user", userInput)
        ));

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        // Call API
        ResponseEntity<ChatResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                ChatResponse.class
        );

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
```

---

# 🌐 6️⃣ Controller

```java
@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final OpenAIService openAIService;

    public LLMController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        String response = openAIService.askLLM(question);
        return ResponseEntity.ok(response);
    }
}
```

---

# 🧪 7️⃣ Test It

## Request

```bash
curl -X POST http://localhost:8080/api/llm/ask \
-H "Content-Type: application/json" \
-d "What is Spring Boot?"
```

---

## Response

```
Spring Boot is a Java framework used to build production-ready applications...
```

---

# ⚠️ 8️⃣ Improvements 

## Add:

### ✅ Timeout handling

```java
restTemplate.setRequestFactory(...)
```

### ✅ Error handling

* Handle 429 (rate limit)
* Retry with backoff

### ✅ Logging

* Log prompt + response (carefully)

### ✅ Streaming (advanced)

* Use WebClient instead of RestTemplate

---

# 🔥 Interview-Level Insight

Say this:

> I expose an API via Spring Boot that calls an LLM using REST. 
I build structured prompts, send them to the model, and parse structured responses. 
In production, I would replace RestTemplate with WebClient for non-blocking I/O and add retries, caching, and rate limiting.

---

# 🚀 Optional Upgrade (Recommended)

Switch to:

* `WebClient` (reactive, better for high throughput)
* Add:

  * Redis cache
  * Circuit breaker (Resilience4j)

---


