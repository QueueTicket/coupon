package com.qticket.coupon.infrastructure.couponevent.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import com.qticket.coupon.domain.couponevent.repository.CouponEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CouponEventRepositoryImpl implements CouponEventRepository {

    private final CouponEventJpaRepository couponEventJpaRepository;

    @Override
    public List<CouponEvent> findAllByCoupon(Coupon coupon) {
        return couponEventJpaRepository.findAllByCoupon(coupon);
    }

    @Override
    public void saveAll(List<CouponEvent> couponEvents) {
        couponEventJpaRepository.saveAll(couponEvents);
    }

    @Override
    public Optional<CouponEvent> findByCouponAndEventId(Coupon coupon, UUID eventId) {
        return couponEventJpaRepository.findByCouponAndEventId(coupon, eventId);
    }
}
