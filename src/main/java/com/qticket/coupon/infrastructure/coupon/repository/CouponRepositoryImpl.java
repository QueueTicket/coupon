package com.qticket.coupon.infrastructure.coupon.repository;

import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetIssuedCouponResponseDto;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import com.qticket.coupon.infrastructure.coupon.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponQueryDSLRepository couponQueryDSLRepository;
    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findById(UUID couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Page<GetCouponResponseDto> getCoupons(String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable) {
        return couponQueryDSLRepository.getCoupons(search, isDeleted, couponTarget, status, pageable);
    }

    @Override
    public Page<GetIssuedCouponResponseDto> getIssuedCoupons(Long userId, String isDeleted, CouponTarget couponTarget, String usable, UUID eventId, Pageable pageable) {
        return couponQueryDSLRepository.getIssuedCoupons(userId,  isDeleted, couponTarget, usable, eventId, pageable);
    }




}
