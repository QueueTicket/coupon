package com.qticket.coupon.infrastructure.couponevent.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponEventJpaRepository extends JpaRepository<CouponEvent, UUID>{


    List<CouponEvent> findAllByCoupon(Coupon coupon);

    Optional<CouponEvent> findByCouponAndEventId(Coupon coupon, UUID eventId);
}
