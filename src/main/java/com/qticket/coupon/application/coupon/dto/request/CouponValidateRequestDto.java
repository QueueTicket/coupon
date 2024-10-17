package com.qticket.coupon.application.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CouponValidateRequestDto {
    @NotNull(message = "유저 ID를 입력해주세요.")
    private Long userId;

    @NotNull(message = "쿠폰 ID를 입력해주세요.")
    private UUID couponId;

    @NotNull(message = "event ID를 입력해주세요.")
    private UUID eventId;

    @NotNull(message = "상품 가격을 입력해주세요.")
    private Long price;
}
