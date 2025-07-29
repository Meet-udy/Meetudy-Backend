package com.api.meetudy;

import com.api.meetudy.chat.dto.ChatPayload;
import com.api.meetudy.global.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class TestMessageListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private ChatPayload receivedPayload;

    @RabbitListener(queues = RabbitMqConfig.CHAT_QUEUE)
    public void receiveMessage(ChatPayload payload) {
        this.receivedPayload = payload;
        System.out.println("Received message: " + payload);
        latch.countDown();
    }

    public boolean awaitMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public ChatPayload getReceivedPayload() {
        return receivedPayload;
    }

}