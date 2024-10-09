package com.qticket.coupon.application.coupon.dto.request;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CouponCreateRequestDto {
    private String name;
    private int discountAmount;
    private DiscountPolicy discountPolicy;
    private CouponTarget target;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private int minSpendAmount;
    private int maxQuantity;
    private int usageLimit;
    private List<UUID> eventId;
    private List<UUID> seatId;
    private String seatGrade;
}
