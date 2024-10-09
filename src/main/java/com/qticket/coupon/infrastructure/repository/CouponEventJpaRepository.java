package com.qticket.coupon.infrastructure.repository;

import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import com.qticket.coupon.domain.couponevent.repository.CouponEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CouponEventJpaRepository extends JpaRepository<CouponEvent, UUID>, CouponEventRepository {
    void saveAll(List<CouponEvent> couponEvents);
}
