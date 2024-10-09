package com.qticket.coupon.infrastructure.couponuser.repository;

import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponUserJpaRepository extends JpaRepository<CouponUser, UUID>, CouponUserRepository {
}
