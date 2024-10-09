package com.qticket.coupon.infrastructure.repository;

import com.qticket.coupon.domain.couponseat.model.CouponSeat;
import com.qticket.coupon.domain.couponseat.repository.CouponSeatRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponSeatJpaRepository extends JpaRepository<CouponSeat, UUID>, CouponSeatRepository {
}
