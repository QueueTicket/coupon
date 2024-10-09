package com.qticket.coupon.domain.coupon.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(UUID couponId);
}
