# **GenAI roadmap** - Module 2 — API Integration (Java)


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


# Using Spring AI Starter


```
    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://spring.io</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.ai</groupId>
                <artifactId>spring-ai-bom</artifactId>
                <version>1.0.0</version> <!-- Use 1.0.0 ou superior para a versão GA -->
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
	
	<dependencies>
	        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-openai</artifactId>
        </dependency>
		
	</dependencies>

```


# Using Spring Ollama Starter 


## 1. Project Setup (pom.xml)
/ The following configuration includes the necessary BOM and the correct Ollama dependency: 
	* ***spring-ai-ollama-spring-boot-starter***.

```
<dependencies>
    <!-- Correct Ollama Starter -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0-M5</version> <!-- Use latest Milestone -->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<repositories>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>https://spring.io</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

## 2. Configuration (application.properties)
* Specify your local Ollama instance and the model you want to use 
	* (e.g., llama3.2 or mistral). 

spring.application.name=ollama-demo

# Ollama configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2

## 3. Simple Controller
Use the ChatClient to send prompts to your local model. 
```
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestControllerpublic class AIController {

    private final ChatClient chatClient;

    // ChatClient.Builder is autoconfigured by Spring AI
    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ai/generate")
    public String generate(@RequestParam(defaultValue = "Tell me a joke about Java") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
```

## 4. Running the Project

   1. Install Ollama: Download it from the [official Ollama website](https://ollama.com/).
   2. Pull the Model: Open your terminal and run:
   ollama pull llama3.2
   3. Start the App: Run your Spring Boot application and test it at:
   http://localhost:8080/ai/generate?message=Hello [1, 4] 

## 4. Resources

[1] [https://dzone.com](https://dzone.com/articles/generate-unit-tests-ai-ollama-spring-boot)
[2] [https://medium.com](https://medium.com/@parsairohit2/spring-ai-integration-guide-connect-llms-openai-anthropic-ollama-to-java-spring-boot-600eb12cc8d0)
[3] [https://medium.com](https://medium.com/@ankitagrahari.rkgit/spring-ai-with-ollama-71f12f6b85bd)
[4] [https://github.com](https://github.com/senoritadeveloper01/nils-spring-ai)

---


# 2. Required Dependency
* Ensure your pom.xml includes ***spring-boot-starter-webflux*** for reactive streaming support. 


# 3. How to Test
* Run your app with Ollama active (e.g., ollama run llama3.2).
* Navigate to http://localhost:8080/ai/stream?message=Tell me a story in your browser.
* Result: The browser displays text incrementally. 

# Key Concepts
--
* .stream(): Replaces .call() to return a Flux<String> rather than a single string.
* Flux<String>: 
	* A reactive container for emitting data chunks.
* SSE: 
	* text/event-stream maintains the connection for real-time, incremental updates. 


## 1. Add WebFlux Dependency
Update your pom.xml to include WebFlux, which is required to handle the Flux (stream) return type:
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

## 2. Update the Controller
Change your method to return Flux<String> and set the produces type to TEXT_EVENT_STREAM_VALUE.

```
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestControllerpublic class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping(value = "/ai/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam(defaultValue = "Write a short poem about coding") String message) {
        return chatClient.prompt()
                .user(message)
                .stream() // This enables the streaming mode
                .content();
    }
}
```
## 3. How to test it

   1. Start Ollama: Ensure the model is running (ollama run llama3.2).
   2. Run your App: Start your Spring Boot application.
   3. Open Browser: Go to http://localhost:8080/ai/stream?message=Tell me a long story.
   4. Observe: You will see the text appearing word-by-word (or chunk-by-chunk) in the browser instead of waiting for the full response.

## Why use this?

* User Experience: The user sees progress immediately, making the app feel much faster.
* Efficiency: It prevents the HTTP connection from timing out during very long LLM generations.

# simple HTML frontend with JavaScript

## Frontend (src/main/resources/static/index.html)

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Spring AI + Ollama Stream</title>
    <style>
        body { font-family: sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
        #chat-box { border: 1px solid #ccc; padding: 20px; min-height: 200px; background: #f9f9f9; white-space: pre-wrap; }
        button { padding: 10px 20px; cursor: pointer; background: #007bff; color: white; border: none; border-radius: 5px; }
        input { padding: 10px; width: 70%; margin-bottom: 10px; }
    </style>
</head>
<body>
    <h1>Ollama AI Stream</h1>
    <input type="text" id="userInput" placeholder="Ask something..." value="Write a short story about a robot">
    <button onclick="startStream()">Send Prompt</button>
    
    <h3>Response:</h3>
    <div id="chat-box"></div>

    <script>
        async function startStream() {
            const input = document.getElementById('userInput').value;
            const chatBox = document.getElementById('chat-box');
            chatBox.innerText = ""; // Limpar resposta anterior

            // Chamada para o endpoint do Spring Boot
            const response = await fetch(`/ai/stream?message=${encodeURIComponent(input)}`);
            const reader = response.body.getReader();
            const decoder = new TextDecoder();

            while (true) {
                const { done, value } = await reader.read();
                if (done) break;
                
                // O Spring AI envia eventos SSE (data: ...)
                const chunk = decoder.decode(value, { stream: true });
                
                // Limpar prefixos 'data:' que o SSE costuma enviar
                const cleanChunk = chunk.replace(/^data:/gm, '').trim();
                
                chatBox.innerText += cleanChunk;
            }
        }
    </script>
</body>
</html>
```


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


