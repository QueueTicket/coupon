package com.qticket.coupon.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic adminIssueCouponTopic() {
        return TopicBuilder.name("admin-coupon-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic customerIssueCouponTopic() {
        return TopicBuilder.name("customer-coupon-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        // 3번 재시도, 재시도 간격 1초
        return new DefaultErrorHandler(new FixedBackOff(1000L, 3));
    }
}
