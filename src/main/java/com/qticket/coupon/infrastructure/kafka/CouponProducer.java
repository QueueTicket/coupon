package com.qticket.coupon.infrastructure.kafka;

import com.qticket.coupon.application.coupon.dto.request.IssueByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByCustomerRequestDto;
import com.qticket.coupon.application.message.Producer;
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
    public void sendIssueByAdminMessage(Long userId, String userRole, IssueByAdminRequestDto issueRequestDto) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(issueRequestDto)
                        .setHeader(KafkaHeaders.TOPIC, "admin-coupon-topic")
                        .setHeader("X-USER-ID", userId)
                        .setHeader("X-USER-ROLE", userRole)
                        .build());
    }

    @Override
    public void sendIssueByCustomerMessage(Long userId, String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(issueByCustomerRequestDto)
                        .setHeader(KafkaHeaders.TOPIC, "customer-coupon-topic")
                        .setHeader("X-USER-ID", userId)
                        .setHeader("X-USER-ROLE", userRole)
                        .build());
    }
}
