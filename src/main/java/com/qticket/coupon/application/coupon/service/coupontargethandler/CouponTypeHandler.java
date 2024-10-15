package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetIssuedCouponResponseDto;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponuser.model.CouponUser;

import java.util.UUID;


public interface CouponTypeHandler {

    Coupon create(CouponCreateRequestDto couponCreateRequestDto);

    CouponTarget getType();

    GetCouponResponseDto getCouponResponseDto(Coupon coupon);

    GetIssuedCouponResponseDto getIssuedCouponResponseDto(Coupon coupon, CouponUser couponUser);

    void validate(Long userId, Coupon coupon, UUID eventId);


    static Coupon toEntity(CouponCreateRequestDto requestDto) {
        return Coupon.create(
                requestDto.getName(),
                requestDto.getDiscountAmount(),
                requestDto.getDiscountPolicy(),
                requestDto.getMaxDiscountPrice(),
                requestDto.getTarget(),
                requestDto.getStartDate(),
                requestDto.getExpirationDate(),
                requestDto.getMinSpendAmount(),
                requestDto.getMaxQuantity(),
                requestDto.getUsageLimit());
    }
}
