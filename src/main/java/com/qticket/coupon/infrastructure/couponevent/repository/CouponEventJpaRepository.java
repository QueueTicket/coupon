package com.qticket.coupon.infrastructure.couponevent.repository;

import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponEventJpaRepository extends JpaRepository<CouponEvent, UUID>{


}
