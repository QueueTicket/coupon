package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.exception.InvalidCouponTargetException;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponAllTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponEventTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistry;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;

import java.util.EnumMap;
import java.util.Map;

public class FakeCouponTypeRegistry implements CouponTypeRegistry {

    protected Map<CouponTarget, CouponTypeHandler> couponTypeHandlerMap = new EnumMap<>(CouponTarget.class);

    public FakeCouponTypeRegistry() {
        couponTypeHandlerMap.put(CouponTarget.ALL, new CouponAllTypeHandler(FakeCouponRepository.getInstance()));
        couponTypeHandlerMap.put(CouponTarget.EVENT, new CouponEventTypeHandler(FakeCouponRepository.getInstance(), FakeCouponEventRepository.getInstance(), new FakeEventServiceClientImpl()));
    }
    @Override
    public CouponTypeHandler getCouponHandler(CouponTarget couponTarget) {
        if (!support(couponTarget)) {
            throw new InvalidCouponTargetException();
        }
        return couponTypeHandlerMap.get(couponTarget);
    }

    @Override
    public boolean support(CouponTarget couponTarget) {
        return couponTypeHandlerMap.containsKey(couponTarget);
    }
}
