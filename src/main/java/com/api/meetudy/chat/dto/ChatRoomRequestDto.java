package com.api.meetudy.chat.dto;

import com.api.meetudy.member.entity.Member;
import lombok.Getter;

@Getter
public class ChatRoomRequestDto {

    private Member sender;

    private Member receiver;

}