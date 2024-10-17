package com.qticket.coupon.application.coupon.dto.request;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CouponCreateRequestDto {

    @NotEmpty(message = "쿠폰 이름을 적어주세요")
    private String name;

    @Min(value = 1, message = "유효한 할인율 또는 할인 금액을 적어주세요")
    private int discountAmount;

    @NotNull(message = "할인 정책을 적어주세요")
    private DiscountPolicy discountPolicy;

    @Min(value = 0, message = "최대 할인 금액을 적어주세요")
    private int maxDiscountPrice;

    @NotNull(message = "쿠폰 적용 타겟을 입력해주세요.")
    private CouponTarget target;

    @NotNull(message = "쿠폰 발급 가능 시작 날짜를 입력해주세요.")
    private LocalDateTime startDate;

    @NotNull(message = "쿠폰 만료 날짜를 입력해주세요.")
    private LocalDateTime expirationDate;

    @Min(value = 0, message = "쿠폰이 적용 가능한 최소 금액을 적어주세요")
    private int minSpendAmount;

    @Min(value = -1, message = "정확한 쿠폰 발급 수량을 적어주세요")
    private int maxQuantity;

    @Min(value = 1, message = "정확한 쿠폰 사용 가능 횟수를 적어주세요")
    private int usageLimit;
    private List<UUID> eventId;
}
