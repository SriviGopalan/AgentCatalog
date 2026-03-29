package org.example.controller;

import org.example.chat.ChatRequest;
import org.example.chat.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    // Key used by Spring AI 1.0.3 MessageChatMemoryAdvisor to look up conversation ID
    private static final String CONVERSATION_ID_KEY = "chat_memory_conversation_id";

    private final ChatClient chatClient;

    public ChatController(ChatClient bankingChatClient) {
        this.chatClient = bankingChatClient;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String sessionId = request.sessionId() != null ? request.sessionId() : "default";
        String response = chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID_KEY, sessionId))
                .user(request.message())
                .call()
                .content();
        return new ChatResponse(response, sessionId);
    }
}
