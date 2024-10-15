package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.dto.request.*;
import com.qticket.coupon.application.coupon.dto.response.*;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CouponService {

    CouponCreateResponseDto create(Long userId, String userRole, CouponCreateRequestDto couponCreateRequestDto);

    CouponDeleteResponseDto delete(Long userId, String userRole, UUID couponId);

    CouponUpdateResponseDto update(Long userId, String userRole, UUID couponId, CouponUpdateRequestDto couponUpdateRequestDto);

    void sendIssueByAdminMessage(Long userId, String userRole, IssueByAdminRequestDto issueByAdminRequestDto);
    void issueByAdmin(IssueByAdminRequestDto issueByAdminRequestDto);

    void sendIssueByCustomer(Long userId, String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto);

    void issueByCustomer(Long userId, IssueByCustomerRequestDto issueByCustomerRequestDto);

    GetCouponResponseDto getCoupon(String userRole, UUID couponId);

    GetIssuedCouponResponseDto getIssuedCoupon(Long currentUserId, String currentUserRole, UUID couponId, Long userId);

    void apply(Long currentUserId, String currentUserRole, UUID couponId);

    Page<GetCouponResponseDto> getCoupons(String currentUserRole, String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable);

    Page<GetIssuedCouponResponseDto> getIssuedCoupons(Long currentUserId, String currentUserRole, Long userId, String isDeleted, CouponTarget couponTarget, String usable, UUID eventId, Pageable pageable);

    Page<GetCouponByAdminRequestDto> getCouponsByAdmin(Long currentUserId, String currentUserRole, Long userId, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable);

    void validate(Long userId, UUID couponId, UUID eventId, Long price);
}