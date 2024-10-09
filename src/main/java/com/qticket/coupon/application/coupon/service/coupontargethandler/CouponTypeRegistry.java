package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;

public interface CouponTypeRegistry {

    CouponTypeHandler getCouponHandler(CouponTarget couponTarget);

    boolean support(CouponTarget couponTarget);
}
