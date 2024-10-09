package com.qticket.coupon.application.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CouponDeleteResponseDto {
    private UUID couponId;
}
