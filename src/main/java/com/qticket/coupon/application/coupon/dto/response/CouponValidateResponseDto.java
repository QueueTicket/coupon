package com.qticket.coupon.application.coupon.dto.response;

import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponValidateResponseDto {

    private UUID couponId;
    private int discountAmount;
    private DiscountPolicy discountPolicy;
    private int maxDiscountAmount;
}
