package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponDeleteRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponUpdateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponUpdateResponseDto;

import java.util.UUID;

public interface CouponService {

    CouponCreateResponseDto create(Long userId, String userRole, CouponCreateRequestDto couponCreateRequestDto);

    CouponDeleteResponseDto delete(Long userId, String userRole, CouponDeleteRequestDto couponDeleteRequestDto);

    CouponUpdateResponseDto update(Long userId, String userRole, UUID couponId, CouponUpdateRequestDto couponUpdateRequestDto);

    void sendIssueMessage(Long userId, String userRole, IssueRequestDto issueRequestDto);
    void issue(IssueRequestDto issueRequestDto);
}