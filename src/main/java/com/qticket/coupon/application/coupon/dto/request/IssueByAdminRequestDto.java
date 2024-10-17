package com.qticket.coupon.application.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class IssueByAdminRequestDto {
    @NotNull(message = "유저 ID를 입력해주세요.")
    private Long userId;
    @NotNull(message = "쿠폰 ID를 입력해주세요.")
    private UUID couponId;
}
