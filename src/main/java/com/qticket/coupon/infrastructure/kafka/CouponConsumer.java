package com.qticket.coupon.infrastructure.kafka;

import com.qticket.common.config.AuditorAwareImpl;
import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;
import com.qticket.coupon.application.coupon.service.CouponService;
import com.qticket.coupon.application.coupon.service.couponmessage.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponConsumer implements Consumer {

    private final CouponService couponService;

    @Override
    @KafkaListener(topics = "coupon-topic", groupId = "coupon-group")
    public void listenIssueCouponMessage(@Header("X-USER-ID") String userId, @Header("X-USER-ROLE") String userRole, IssueRequestDto issueRequestDto) {

        couponService.issue(issueRequestDto);

    }
}
