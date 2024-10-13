package com.qticket.coupon.domain.couponuser.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponuser.model.CouponUser;

import java.util.List;
import java.util.Optional;

public interface CouponUserRepository {

    CouponUser save(CouponUser couponUser);
    Optional<CouponUser> findByUserIdAndCoupon(Long userId, Coupon coupon);


}
