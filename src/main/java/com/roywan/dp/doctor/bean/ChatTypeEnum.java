package com.roywan.dp.doctor.bean;

/**
 * 回话记录的内容
 */
public enum ChatTypeEnum {
    USER("user","用户发的内容"),
    BOT("bot","AI回复的内容");

     public String type;
    public String value;

    ChatTypeEnum(String type, String value) {
            this.type = type;
          this.value =value;
    }
}
