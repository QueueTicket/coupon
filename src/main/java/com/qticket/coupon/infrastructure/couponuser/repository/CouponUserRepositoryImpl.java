package com.qticket.coupon.infrastructure.couponuser.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponUserRepositoryImpl implements CouponUserRepository {

    private final CouponUserJpaRepository couponUserJpaRepository;

    @Override
    public CouponUser save(CouponUser couponUser) {
        return couponUserJpaRepository.save(couponUser);
    }

    @Override
    public List<CouponUser> findAllByUserIdAndCoupon(Long userId, Coupon coupon) {
        return couponUserJpaRepository.findAllByUserIdAndCoupon(userId, coupon);
    }
}
