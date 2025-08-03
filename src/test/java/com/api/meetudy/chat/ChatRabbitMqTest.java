package com.api.meetudy.chat;

import com.api.meetudy.chat.dto.ChatPayloadDto;
import com.api.meetudy.chat.enums.MessageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ChatRabbitMqTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ChatTestListener chatTestListener;

    private static final String EXCHANGE = "chat.exchange";
    private static final String ROUTING_KEY = "chat.routing.key";

    @Test
    void sendTestMessageAndReceive() throws Exception {
        ChatPayloadDto payload = new ChatPayloadDto(1L, 1L, "Hello from test", MessageType.TALK);

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, payload);
        System.out.println("Sent message: " + payload);

        boolean messageReceived = chatTestListener.awaitMessage(10, TimeUnit.SECONDS);

        assertTrue(messageReceived, "Message was not received within timeout");

        ChatPayloadDto received = chatTestListener.getReceivedPayload();
        Assertions.assertNotNull(received);
        assertEquals(payload.getMessage(), received.getMessage());
        assertEquals(payload.getRoomId(), received.getRoomId());
        assertEquals(payload.getSenderId(), received.getSenderId());
    }

}