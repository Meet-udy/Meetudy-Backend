package com.api.meetudy.notifiaction;

import com.api.meetudy.notification.dto.NotificationPayloadDto;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationRabbitMqTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NotificationTestListener testListener;

    private static final String EXCHANGE = "notification.exchange";
    private static final String ROUTING_KEY = "notification.routing.key";

    @Test
    void sendNotificationAndReceive() throws Exception {
        NotificationPayloadDto payload = NotificationPayloadDto.builder()
                .receiverId(1L)
                .message("Test notification")
                .postId(100L)
                .build();

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, payload);
        System.out.println("Sent notification: " + payload);

        boolean received = testListener.awaitMessage(10, TimeUnit.SECONDS);

        assertTrue(received, "Notification message was not received within timeout");

        NotificationPayloadDto receivedPayload = testListener.getReceivedPayload();
        assertNotNull(receivedPayload);
        assertEquals(payload.getReceiverId(), receivedPayload.getReceiverId());
        assertEquals(payload.getMessage(), receivedPayload.getMessage());
        assertEquals(payload.getPostId(), receivedPayload.getPostId());
    }

}