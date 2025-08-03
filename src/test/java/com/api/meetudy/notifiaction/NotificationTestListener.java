package com.api.meetudy.notifiaction;

import com.api.meetudy.global.config.RabbitMqConfig;
import com.api.meetudy.notification.dto.NotificationPayloadDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationTestListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private NotificationPayloadDto receivedPayload;

    @RabbitListener(queues = RabbitMqConfig.NOTIFICATION_QUEUE)
    public void receive(NotificationPayloadDto payload) {
        this.receivedPayload = payload;
        System.out.println("Received notification: " + payload);
        latch.countDown();
    }

    public boolean awaitMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public NotificationPayloadDto getReceivedPayload() {
        return receivedPayload;
    }

}