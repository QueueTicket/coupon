package com.qticket.coupon.application.coupon.dto.response;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import com.qticket.coupon.domain.coupon.model.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class GetCouponResponseDto {
    private UUID couponId;
    private String couponName;
    private DiscountInfo discountInfo;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private int maxQuantity;
    private int usageLimit;
    private CouponTarget target;
    private List<ConcertDto> concertList;
    
    @Getter
    @Setter
    @AllArgsConstructor
    public static class DiscountInfo {
        private int discountAmount;
        private DiscountPolicy discountPolicy;
        private int maxDiscountPrice;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ConcertDto {
        private UUID concertId;
        private String concertName;
    }

    public GetCouponResponseDto(Coupon coupon) {
        DiscountInfo discount = new DiscountInfo(coupon.getDiscountAmount(), coupon.getDiscountPolicy(), coupon.getMaxDiscountPrice());

        couponId = coupon.getId();
        couponName = coupon.getName();
        discountInfo = discount;
        startDate = coupon.getStartDate();
        expirationDate = coupon.getExpirationDate();
        maxQuantity = coupon.getMaxQuantity();
        usageLimit = coupon.getUsageLimit();
        target = coupon.getTarget();
    }

    public GetCouponResponseDto(Coupon coupon, List<ConcertDto> concertDtoList) {
        DiscountInfo discount = new DiscountInfo(coupon.getDiscountAmount(), coupon.getDiscountPolicy(), coupon.getMaxDiscountPrice());

        couponId = coupon.getId();
        couponName = coupon.getName();
        discountInfo = discount;
        startDate = coupon.getStartDate();
        expirationDate = coupon.getExpirationDate();
        maxQuantity = coupon.getMaxQuantity();
        usageLimit = coupon.getUsageLimit();
        target = coupon.getTarget();
        concertList = concertDtoList;
    }
}
