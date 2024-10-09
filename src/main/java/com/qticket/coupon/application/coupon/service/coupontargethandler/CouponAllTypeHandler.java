package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponAllTypeHandler implements CouponTypeHandler {

    private final CouponRepository couponRepository;
    
    @Override
    @Transactional
    public Coupon create(CouponCreateRequestDto couponCreateRequestDto) {
        Coupon coupon = CouponTypeHandler.toEntity(couponCreateRequestDto);
        return couponRepository.save(coupon);
    }

    @Override
    public CouponTarget getType() {
        return CouponTarget.ALL;
    }
    
}
