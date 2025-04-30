package com.roywan.dp.doctor.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;

@RestController
@CrossOrigin(origins = "*")
public class ChatBotController {
    ChatClient chatClient;
    OpenAiChatModel mChatModel;
    public ChatBotController(ChatClient.Builder builder, OpenAiChatModel chatModel) {
       this.chatClient= builder.defaultSystem(
               "你是一个天气预报员，当有人输入日期的时候，你输出苏州的天气预报信息，" +
               "生成结果在html页面中以markdown的格式输出，最后输出结尾的时候始终以下面的语句结尾：感谢您的咨询，我是舆情君。"
       ).build();
       this.mChatModel=chatModel;
    }

    @GetMapping(value="/chat/{message}")
    public String chat(@PathVariable("message") String msg) {
        return chatClient.prompt().user(msg).call().content();
    }

    @GetMapping(value = "/streamDemo",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamDataForDemo(){
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
    @GetMapping(value = "ai/generateStream")
    public SseEmitter streamChat(@RequestParam String message){
        //创建发射器，超时时间1分钟
        SseEmitter emitter=new SseEmitter(60*1000L);
        Prompt prompt=new Prompt(new UserMessage(message));
        mChatModel.stream(prompt).subscribe(chatResponse -> {
            try{
               String content= chatResponse.getResult().getOutput().getText();
                System.out.printf("返回结果:"+content);
                emitter.send(
                        SseEmitter.event()
                                .data(content)
                                .id(String.valueOf(System.currentTimeMillis()))
                                .build());

            }catch (Exception e){
                emitter.completeWithError(e);
            }
        },error->{
            System.out.printf("链接失败 e:"+error);
            emitter.completeWithError(error);
        },()->{
            System.out.println("处理完成");
            emitter.complete();
        });
        return emitter;
    }
}
