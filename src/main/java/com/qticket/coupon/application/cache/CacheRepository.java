package com.qticket.coupon.application.cache;

import com.qticket.coupon.domain.coupon.model.Coupon;

import java.util.UUID;

public interface CacheRepository {

    void save(Coupon coupon);

    Integer getCouponQuantityById(UUID couponId);

    boolean isExistByCouponIdAndUserId(UUID couponId, Long userId);

    Long decreaseCouponQuantity(UUID couponId);

    void issueCoupon(UUID couponId, Long userId);

    void increaseCouponQuantity(UUID couponId);
}
