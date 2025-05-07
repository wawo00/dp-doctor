package com.roywan.dp.doctor.controller;

import com.roywan.dp.doctor.service.ChatBoxService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("ai")
public class ChatBotController {
    @Autowired
    ChatBoxService chatBoxService;
    @GetMapping(value="/chat/{message}")
    public String chat(@PathVariable("message") String msg) {
        return chatBoxService.defChat(msg);
    }
    @GetMapping(value = "/generateStream")
    public SseEmitter streamChat(@RequestParam String message){
       return chatBoxService.streamChat(message);
    }

    @GetMapping(value = "/getRecords")
    public Object getRecords(@RequestParam String who){
        return chatBoxService.getChatRecord(who);
    }
}
