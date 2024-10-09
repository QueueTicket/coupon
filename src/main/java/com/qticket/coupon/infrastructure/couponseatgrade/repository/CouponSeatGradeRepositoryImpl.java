package com.qticket.coupon.infrastructure.couponseatgrade.repository;

import com.qticket.coupon.domain.couponseatgrade.repository.CouponSeatGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponSeatGradeRepositoryImpl implements CouponSeatGradeRepository {

    private final CouponSeatGradeJpaRepository couponSeatGradeJpaRepository;
}
