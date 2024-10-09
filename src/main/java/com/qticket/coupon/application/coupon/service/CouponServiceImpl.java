package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponDeleteRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponUpdateRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponUpdateResponseDto;
import com.qticket.coupon.application.coupon.exception.ConcertIdNotRequiredException;
import com.qticket.coupon.application.coupon.exception.CouponNotFoundException;
import com.qticket.coupon.application.coupon.exception.InvalidConcertIdException;
import com.qticket.coupon.application.coupon.exception.UnauthorizedAccessException;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    @Transactional
    public CouponCreateResponseDto create(Long userId, String userRole, CouponCreateRequestDto requestDto) {
        Coupon coupon = toEntity(requestDto);
        isValid(userRole, coupon);
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponCreateResponseDto(savedCoupon.getId());

    }

    private Coupon toEntity(CouponCreateRequestDto requestDto) {
        return Coupon.create(
                requestDto.getName(),
                requestDto.getDiscountAmount(),
                requestDto.getDiscountPolicy(),
                requestDto.getTarget(),
                requestDto.getStartDate(),
                requestDto.getExpirationDate(),
                requestDto.getMinSpendAmount(),
                requestDto.getUsageLimit(),
                requestDto.getMaxQuantity());
    }

    private void isValid(String userRole, Coupon coupon) {
        isAdminUser(userRole);
        isValidCoupon(coupon);
    }

    private void isAdminUser(String userRole) {
        if (!"ADMIN".equals(userRole)) {
            throw new UnauthorizedAccessException();
        }
    }

    private void isValidCoupon(Coupon coupon) {
        if (CouponTarget.ALL.equals(coupon.getTarget()) && coupon.getConcertId() != null) {
            throw new ConcertIdNotRequiredException();
        }

        if (CouponTarget.CONCERT.equals(coupon.getTarget()) && coupon.getConcertId() == null) {
            throw new InvalidConcertIdException();
        }
    }

    @Override
    @Transactional
    public CouponDeleteResponseDto delete(Long userId, String userRole, CouponDeleteRequestDto couponDeleteRequestDto) {
        isAdminUser(userRole);
        Coupon coupon = getCouponById(couponDeleteRequestDto.getCouponId());
        coupon.softDelete(String.valueOf(userId));
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponDeleteResponseDto(savedCoupon.getId());
    }

    private Coupon getCouponById(UUID couponId) {
        return couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);
    }

    @Override
    @Transactional
    public CouponUpdateResponseDto update(Long userId, String userRole, UUID couponId, CouponUpdateRequestDto couponUpdateRequestDto) {
        isAdminUser(userRole);
        Coupon coupon = getCouponById(couponId);
        update(coupon, couponUpdateRequestDto);
        couponRepository.save(coupon);
        return new CouponUpdateResponseDto(coupon);
    }

    private void update(Coupon coupon, CouponUpdateRequestDto updateRequestDto) {
        coupon.update(
                updateRequestDto.getName(),
                updateRequestDto.getDiscountAmount(),
                updateRequestDto.getDiscountPolicy(),
                updateRequestDto.getTarget(),
                updateRequestDto.getStartDate(),
                updateRequestDto.getExpirationDate(),
                updateRequestDto.getMinSpendAmount(),
                updateRequestDto.getUsageLimit(),
                updateRequestDto.getMaxQuantity());
    }

}
