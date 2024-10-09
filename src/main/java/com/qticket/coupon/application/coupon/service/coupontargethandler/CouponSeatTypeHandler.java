package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;

public class CouponSeatTypeHandler implements CouponTypeHandler {
    @Override
    public Coupon create(CouponCreateRequestDto couponCreateRequestDto) {
        return null;
    }

    @Override
    public CouponTarget getType() {
        return null;
    }
}
