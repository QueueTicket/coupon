package com.qticket.coupon.application.coupon.dto.response;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import com.qticket.coupon.domain.coupon.model.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CouponUpdateResponseDto {

    private UUID couponId;
    private String name;
    private int discountAmount;
    private DiscountPolicy discountPolicy;
    private CouponTarget target;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private int minSpendAmount;
    private int maxQuantity;
    private int usageLimit;
    
    public CouponUpdateResponseDto(Coupon coupon){
        this.couponId = coupon.getId();
        this.name = coupon.getName();
        this.discountAmount = coupon.getDiscountAmount();
        this.discountPolicy = coupon.getDiscountPolicy();
        this.target = coupon.getTarget();
        this.startDate = coupon.getStartDate();
        this.expirationDate = coupon.getExpirationDate();
        this.minSpendAmount = coupon.getMinSpendAmount();
        this.maxQuantity = coupon.getMaxQuantity();
        this.usageLimit = coupon.getUsageLimit();
    }
}

