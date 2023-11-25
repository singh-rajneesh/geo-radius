package com.project.georadiusservice.config.rabbit;

import com.climate.config.QueueNames;
import com.climate.config.RabbitMQConstants;
import com.climate.event.producer.RabbitMQConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.climate.event")
@EnableRabbit
public class RabbitConfig extends RabbitMQConfiguration {

    @Override
    protected List<String> getQueueNames() {
        List<String> queues = new ArrayList<>();
        queues.add(OrderIntentQueueNames.ORDER_INTENT_PROCESSING_QUEUE);
        queues.add(OrderIntentQueueNames.ORDER_INTENT_ADVISOR_REASSIGN_QUEUE);
        queues.add(OrderIntentQueueNames.ORDER_PROCESSING_QUEUE);
        queues.add(OrderIntentQueueNames.S3_DATA_PROCESSING_QUEUE_DLQ);
        queues.add(OrderIntentQueueNames.ADVISOR_WHATSAPP_USER_CREATION_QUEUE);
        queues.add(OrderIntentQueueNames.WHATSAPP_EVENT_QUEUE);
        queues.add(QueueNames.AMS_ORDER_LINKING_QUEUE);
        queues.add(OrderIntentQueueNames.PURCHASE_FULFILLMENT_QUEUE);
        queues.add(OrderIntentQueueNames.PRODUCT_PURCHASED_QUEUE);
        queues.add(OrderIntentQueueNames.PURCHASED_REWARDS_QUEUE);
        queues.add(OrderIntentQueueNames.FARMER_PROFILE_IMAGE_DELETION_QUEUE);
        return queues;
    }

    @Bean
    public Binding s3DataProcessingQueueBinding() {
        return BindingBuilder.bind(s3DataProcessingQueue())
                .to(exchange())
                .with(String.join(".", "routingkey", OrderIntentQueueNames.S3_DATA_PROCESSING_QUEUE));
    }

    @Bean
    public Queue s3DataProcessingQueue() {
        return QueueBuilder.durable(OrderIntentQueueNames.S3_DATA_PROCESSING_QUEUE)
                .withArgument(RabbitMQConstants.X_DEAD_LETTER_EXCHANGE, exchange().getName())
                .withArgument(RabbitMQConstants.X_DEAD_LETTER_ROUTING_KEY,
                        String.join(".", "routingkey", OrderIntentQueueNames.S3_DATA_PROCESSING_QUEUE_DLQ))
                .build();
    }

}