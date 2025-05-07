package com.roywan.dp.doctor.service;

import com.roywan.dp.doctor.bean.ChatRecord;
import com.roywan.dp.doctor.bean.ChatTypeEnum;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChatRecordService {
    public void saveChatRecord(String userName, String message, ChatTypeEnum chatTypeEnum);
    public List<ChatRecord> getChatRecord(String who);
}
