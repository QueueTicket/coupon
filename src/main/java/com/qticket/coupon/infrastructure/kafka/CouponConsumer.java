package com.qticket.coupon.infrastructure.kafka;

import com.qticket.coupon.application.cache.CacheRepository;
import com.qticket.coupon.application.coupon.dto.request.IssueByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByCustomerRequestDto;
import com.qticket.coupon.application.coupon.exception.AlreadyIssuedUserException;
import com.qticket.coupon.application.coupon.exception.CouponNotFoundException;
import com.qticket.coupon.application.coupon.service.CouponService;
import com.qticket.coupon.application.message.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponConsumer implements Consumer {

    private final CouponService couponService;
    private final CacheRepository cacheRepository;

    @Override
    @KafkaListener(topics = "admin-coupon-topic", groupId = "coupon-group")
    public void listenIssueByAdminMessage(@Header("X-USER-ID") String userId, @Header("X-USER-ROLE") String userRole, IssueByAdminRequestDto issueByAdminRequestDto) {

        couponService.issueByAdmin(issueByAdminRequestDto);

    }

    @Override
    @KafkaListener(topics = "customer-coupon-topic", groupId = "coupon-group")
    public void listenIssueByCustomerMessage(@Header("X-USER-ID") String userId, @Header("X-USER-ROLE") String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto) {

        try {
            couponService.IssueByCustomer(Long.parseLong(userId), issueByCustomerRequestDto);
        } catch (AlreadyIssuedUserException e) {
            UUID couponId = issueByCustomerRequestDto.getCouponId();
            if (cacheRepository.getCouponQuantityById(issueByCustomerRequestDto.getCouponId()) != null) {
                cacheRepository.increaseCouponQuantity(issueByCustomerRequestDto.getCouponId());
            }
        }


    }
}
