package com.qticket.coupon.application.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CouponCreateResponseDto {
    private UUID couponId;
}
