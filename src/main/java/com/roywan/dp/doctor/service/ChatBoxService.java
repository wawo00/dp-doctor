package com.roywan.dp.doctor.service;

import com.roywan.dp.doctor.bean.ChatRecord;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public  interface ChatBoxService {

    /**
     * 阻塞式返回ai的内容
     * @param msg
     * @return
     */
    public String defChat(String msg);

    /**
     * 数据流式返回ai的内容
     * @param msg
     * @return
     */
    public SseEmitter streamChat(String msg);


    public List<ChatRecord> getChatRecord(String who);
}
