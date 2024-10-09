package com.qticket.coupon.infrastructure.kafka;

import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;
import com.qticket.coupon.application.coupon.service.couponmessage.Producer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class CouponProducer implements Producer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CouponProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendIssueCouponMessage(Long userId, String userRole, IssueRequestDto issueRequestDto) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(issueRequestDto)
                        .setHeader(KafkaHeaders.TOPIC, "coupon-topic")
                        .setHeader("X-USER-ID", userId)
                        .setHeader("X-USER-ROLE", userRole)
                        .build());
    }
}
