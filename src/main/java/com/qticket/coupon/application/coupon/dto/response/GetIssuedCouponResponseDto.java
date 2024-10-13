package com.qticket.coupon.application.coupon.dto.response;


import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetIssuedCouponResponseDto {
    private UUID couponId;
    private String couponName;
    private GetCouponResponseDto.DiscountInfo discountInfo;
    private LocalDateTime issuedDate;
    private LocalDateTime expirationDate;
    private int usageCount;
    private int usageLimit;
    private CouponTarget target;
    private List<GetCouponResponseDto.ConcertDto> concertList;
    private boolean usable = true;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DiscountInfo {
        private int discountAmount;
        private DiscountPolicy discountPolicy;
        private int maxDiscountPrice;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ConcertDto {
        private UUID concertId;
        private String concertName;
    }

    private void updateUsable() {
        if (isExpired() || validateUnusable()) {
            usable = false;
        }
    }
    private boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDateTime.now());
    }

    private boolean validateUnusable() {
        return usageCount >= usageLimit;
    }

    public GetIssuedCouponResponseDto(Coupon coupon, CouponUser couponUser) {
        GetCouponResponseDto.DiscountInfo discount = new GetCouponResponseDto.DiscountInfo(coupon.getDiscountAmount(), coupon.getDiscountPolicy(), coupon.getMaxDiscountPrice());

        couponId = coupon.getId();
        couponName = coupon.getName();
        discountInfo = discount;
        issuedDate = couponUser.getCreatedAt();
        expirationDate = coupon.getExpirationDate();
        usageCount = couponUser.getUsageCount();
        usageLimit = coupon.getUsageLimit();
        target = coupon.getTarget();
        updateUsable();
    }


    public GetIssuedCouponResponseDto(Coupon coupon, List<GetCouponResponseDto.ConcertDto> concertDtoList, CouponUser couponUser) {
        GetCouponResponseDto.DiscountInfo discount = new GetCouponResponseDto.DiscountInfo(coupon.getDiscountAmount(), coupon.getDiscountPolicy(), coupon.getMaxDiscountPrice());

        couponId = coupon.getId();
        couponName = coupon.getName();
        discountInfo = discount;
        issuedDate = couponUser.getCreatedAt();
        expirationDate = coupon.getExpirationDate();
        usageCount = couponUser.getUsageCount();
        usageLimit = coupon.getUsageLimit();
        target = coupon.getTarget();
        concertList = concertDtoList;
        updateUsable();

    }
}
