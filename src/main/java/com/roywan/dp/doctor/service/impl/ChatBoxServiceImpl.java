package com.roywan.dp.doctor.service.impl;

import com.roywan.dp.doctor.bean.ChatRecord;
import com.roywan.dp.doctor.bean.ChatTypeEnum;
import com.roywan.dp.doctor.service.ChatBoxService;
import com.roywan.dp.doctor.service.ChatRecordService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatBoxServiceImpl implements ChatBoxService {

    ChatClient mChatClient;

    @Autowired
    OpenAiChatModel mChatModel;

    @Autowired
    ChatRecordService chatRecordService;
    //使用构造方法中传入参数的方式初始化成员变量是spring推荐的方式，而不是使用@autoWrited
    public ChatBoxServiceImpl(ChatClient.Builder builder) {
        this.mChatClient= builder.defaultSystem(
                "你是一个天气预报员，当有人输入日期的时候，你输出苏州的天气预报信息，" +
                        "生成结果在html页面中以markdown的格式输出，最后输出结尾的时候始终以下面的语句结尾：感谢您的咨询，我是舆情君。"
        ).build();
    }



    @Override
    public String defChat(String msg) {
        return mChatClient.prompt().user(msg).call().content();
    }

    @Override
    public SseEmitter streamChat(String message) {
        //创建发射器，超时时间1分钟
        SseEmitter emitter=new SseEmitter(60*1000L);
        Prompt prompt=new Prompt(new UserMessage(message));
        chatRecordService.saveChatRecord("me",message, ChatTypeEnum.USER);
        StringBuffer sb=new StringBuffer();
        //视频中的流式响应
//        Flux<ChatResponse> streamResp = mChatModel.stream(prompt);
//        streamResp.toStream().map(chatResponse -> {
//
//            return ""
//        });
//
//        mChatModel.stream(prompt)
//                .map(chatResponse -> {
//            return "";
//        });
        mChatModel.stream(prompt).subscribe(chatResponse -> {
            try{
                String content= chatResponse.getResult().getOutput().getText();
                System.out.printf("返回结果:"+content);
                sb.append(content);
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
            chatRecordService.saveChatRecord("me",sb.toString(),ChatTypeEnum.BOT);
            emitter.complete();
        });
        return emitter;
    }

    @Override
    public List<ChatRecord> getChatRecord(String who) {
        return chatRecordService.getChatRecord(who);
    }
}
