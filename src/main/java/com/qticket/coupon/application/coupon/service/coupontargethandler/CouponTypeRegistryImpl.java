package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.coupon.application.coupon.exception.InvalidCouponTargetException;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class CouponTypeRegistryImpl implements CouponTypeRegistry {
    protected Map<CouponTarget, CouponTypeHandler> couponTypeHandlerMap = new EnumMap<>(CouponTarget.class);

    @Autowired
    public CouponTypeRegistryImpl(List<CouponTypeHandler> couponTypeHandlers) {
        couponTypeHandlers.forEach( couponTypeHandler ->
                couponTypeHandlerMap.put(couponTypeHandler.getType(), couponTypeHandler));
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
