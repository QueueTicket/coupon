package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;

public interface CouponTypeHandler {

    Coupon create(CouponCreateRequestDto couponCreateRequestDto);

    CouponTarget getType();
    
    static Coupon toEntity(CouponCreateRequestDto requestDto) {
        return Coupon.create(
                requestDto.getName(),
                requestDto.getDiscountAmount(),
                requestDto.getDiscountPolicy(),
                requestDto.getTarget(),
                requestDto.getStartDate(),
                requestDto.getExpirationDate(),
                requestDto.getMinSpendAmount(),
                requestDto.getUsageLimit(),
                requestDto.getMaxQuantity());
    }
}
