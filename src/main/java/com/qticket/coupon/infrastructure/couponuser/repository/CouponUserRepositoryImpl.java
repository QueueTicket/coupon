package com.qticket.coupon.infrastructure.couponuser.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponUserRepositoryImpl implements CouponUserRepository {

    private final CouponUserJpaRepository couponUserJpaRepository;

    @Override
    public CouponUser save(CouponUser couponUser) {
        return couponUserJpaRepository.save(couponUser);
    }

    @Override
    public Optional<CouponUser> findByUserIdAndCoupon(Long userId, Coupon coupon) {
        return couponUserJpaRepository.findByUserIdAndCoupon(userId, coupon);
    }
}
