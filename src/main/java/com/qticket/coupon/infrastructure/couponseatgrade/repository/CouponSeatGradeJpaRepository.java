package com.qticket.coupon.infrastructure.couponseatgrade.repository;

import com.qticket.coupon.domain.couponseatgrade.model.CouponSeatGrade;
import com.qticket.coupon.domain.couponseatgrade.repository.CouponSeatGradeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponSeatGradeJpaRepository extends JpaRepository<CouponSeatGrade, UUID>, CouponSeatGradeRepository {
}
