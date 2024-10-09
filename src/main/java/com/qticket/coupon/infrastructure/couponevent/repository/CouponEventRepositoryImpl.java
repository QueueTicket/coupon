package com.qticket.coupon.infrastructure.couponevent.repository;

import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import com.qticket.coupon.domain.couponevent.repository.CouponEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponEventRepositoryImpl implements CouponEventRepository {

    private final CouponEventJpaRepository couponEventJpaRepository;
    @Override
    public void saveAll(List<CouponEvent> couponEvents) {
        couponEventJpaRepository.saveAll(couponEvents);
    }
}
