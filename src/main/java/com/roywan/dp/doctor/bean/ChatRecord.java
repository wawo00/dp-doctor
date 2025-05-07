package com.roywan.dp.doctor.bean;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Data
public class ChatRecord {
    public String id;
    public String content;
    public String chatType;
    public String familyMember;
    public LocalDateTime chatTime;
}
