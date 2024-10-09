package com.qticket.coupon.application.coupon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CouponDeleteRequestDto {

    private UUID couponId;
}
