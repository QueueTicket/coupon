package com.qticket.coupon.application.coupon.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class IssueByAdminRequestDto {
    private Long userId;
    private UUID couponId;
}
