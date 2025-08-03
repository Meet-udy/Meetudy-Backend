package com.api.meetudy.chat;

import com.api.meetudy.chat.dto.ChatPayloadDto;
import com.api.meetudy.global.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class ChatTestListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private ChatPayloadDto receivedPayload;

    @RabbitListener(queues = RabbitMqConfig.CHAT_QUEUE)
    public void receiveMessage(ChatPayloadDto payload) {
        this.receivedPayload = payload;
        System.out.println("Received message: " + payload);
        latch.countDown();
    }

    public boolean awaitMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public ChatPayloadDto getReceivedPayload() {
        return receivedPayload;
    }

}