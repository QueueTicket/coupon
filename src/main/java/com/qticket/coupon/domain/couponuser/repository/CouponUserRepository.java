package com.qticket.coupon.domain.couponuser.repository;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponuser.model.CouponUser;

import java.util.List;

public interface CouponUserRepository {

    CouponUser save(CouponUser couponUser);
    List<CouponUser> findAllByUserIdAndCoupon(Long userId, Coupon coupon);
}
