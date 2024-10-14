package com.qticket.coupon.application.coupon.dto.request;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import com.qticket.coupon.domain.coupon.model.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCouponByAdminRequestDto {

    private UUID couponId;
    private String name;
    private int discountAmount;
    private DiscountPolicy discountPolicy;
    private int maxDiscountPrice;
    private CouponTarget target;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private int minSpendAmount;
    private int maxQuantity;
    private int issuedQuantity;
    private int usageLimit;
    private int usageQuantity ;

    public GetCouponByAdminRequestDto(Coupon coupon) {
        couponId = coupon.getId();
        name = coupon.getName();
        discountAmount = coupon.getDiscountAmount();
        discountPolicy = coupon.getDiscountPolicy();
        maxDiscountPrice = coupon.getMaxDiscountPrice();
        target = coupon.getTarget();
        startDate = coupon.getStartDate();
        expirationDate = coupon.getExpirationDate();
        minSpendAmount = coupon.getMinSpendAmount();
        maxQuantity = coupon.getMaxQuantity();
        issuedQuantity = coupon.getIssuedQuantity();
        usageLimit = coupon.getUsageLimit();
        usageQuantity = coupon.getUsageQuantity();
    }
}
