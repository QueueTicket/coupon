package com.qticket.coupon.infrastructure.couponuser.repository;

import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponUserRepositoryImpl implements CouponUserRepository {

    private final CouponUserJpaRepository couponUserJpaRepository;
}
