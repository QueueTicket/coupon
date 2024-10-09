package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class CouponTypeRegistry {
    protected Map<CouponTarget, CouponTypeHandler> CouponTypeHandlerMap = new EnumMap<>(CouponTarget.class);

}
