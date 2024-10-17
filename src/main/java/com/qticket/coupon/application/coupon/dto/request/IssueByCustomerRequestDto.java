package com.qticket.coupon.application.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class IssueByCustomerRequestDto {

    @NotNull(message = "쿠폰 ID를 입력해주세요.")
    private UUID couponId;
}
