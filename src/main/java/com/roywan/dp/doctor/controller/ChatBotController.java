package com.roywan.dp.doctor.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;

@RestController
@CrossOrigin(origins = "*")
public class ChatBotController {
    ChatClient chatClient;

    public ChatBotController(ChatClient.Builder builder) {
       this.chatClient= builder.defaultSystem(
               "你是一个天气预报员，当有人输入日期的时候，你输出苏州的天气预报信息，" +
               "生成结果在html页面中以markdown的格式输出，最后输出结尾的时候始终以下面的语句结尾：感谢您的咨询，我是舆情君。"
       ).build();
    }

    @GetMapping(value="/chat/{message}")
    public String chat(@PathVariable("message") String msg) {
        return chatClient.prompt().user(msg).call().content();
    }

    @GetMapping(value = "/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamData(){
        //创建发射器，超时时间1分钟
        SseEmitter emitter=new SseEmitter(60*1000L);
        new Thread(()->{
            try{
                for (int i = 0; i < 6; i++) {
                    Thread.sleep(1000);
                    emitter.send("time="+System.currentTimeMillis());
                }

            }catch (Exception e){
                emitter.completeWithError(e);
            }

        }).start();
        return emitter;
    }

}
