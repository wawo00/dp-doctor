package com.roywan.dp.doctor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roywan.dp.doctor.bean.ChatRecord;
import com.roywan.dp.doctor.bean.ChatTypeEnum;
import com.roywan.dp.doctor.mapper.ChatRecordMapper;
import com.roywan.dp.doctor.service.ChatRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRecordServiceImpl implements ChatRecordService {

    @Resource
    ChatRecordMapper chatRecordMapper;

    @Override
    public void saveChatRecord(String userName, String message, ChatTypeEnum chatTypeEnum) {
        ChatRecord cr=new ChatRecord();
        cr.familyMember=userName;
        cr.chatType=chatTypeEnum.type;
        cr.content=message;
        cr.chatTime= LocalDateTime.now();
        chatRecordMapper.insert(cr);
    }

    @Override
    public List<ChatRecord> getChatRecord(String who) {
        QueryWrapper<ChatRecord> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("family_member",who).orderByDesc("chat_time");;
        return chatRecordMapper.selectList(queryWrapper);
    }
}
