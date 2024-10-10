package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.dto.request.*;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponUpdateResponseDto;

import java.util.UUID;

public interface CouponService {

    CouponCreateResponseDto create(Long userId, String userRole, CouponCreateRequestDto couponCreateRequestDto);

    CouponDeleteResponseDto delete(Long userId, String userRole, CouponDeleteRequestDto couponDeleteRequestDto);

    CouponUpdateResponseDto update(Long userId, String userRole, UUID couponId, CouponUpdateRequestDto couponUpdateRequestDto);

    void sendIssueByAdminMessage(Long userId, String userRole, IssueByAdminRequestDto issueByAdminRequestDto);
    void issueByAdmin(IssueByAdminRequestDto issueByAdminRequestDto);

    void sendIssueByCustomer(Long userId, String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto);

    void IssueByCustomer(Long userId, IssueByCustomerRequestDto issueByCustomerRequestDto);
}