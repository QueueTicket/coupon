package com.qticket.coupon.infrastructure.couponseat.repository;

import com.qticket.coupon.domain.couponseat.repository.CouponSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponSeatRepositoryImpl implements CouponSeatRepository {

    private final CouponSeatJpaRepository couponSeatJpaRepository;
}
