package com.qticket.coupon.domain.coupon.repository;

import com.qticket.coupon.application.coupon.dto.request.GetCouponByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetIssuedCouponResponseDto;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(UUID couponId);

    Page<GetCouponResponseDto> getCoupons(String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable);

    Page<GetIssuedCouponResponseDto> getIssuedCoupons(Long userId, String isDeleted, CouponTarget couponTarget, String usable, UUID eventId, Pageable pageable);

    Page<GetCouponByAdminRequestDto> getCouponsByAdmin(Long userId, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable);
}

