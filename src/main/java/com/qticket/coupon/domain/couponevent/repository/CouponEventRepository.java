package com.qticket.coupon.domain.couponevent.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponevent.model.CouponEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface CouponEventRepository {

    List<CouponEvent> findAllByCoupon(Coupon coupon);
    void saveAll(List<CouponEvent> couponEvents);

    Optional<CouponEvent> findByCouponAndEventId(Coupon coupon, UUID eventId);

}
