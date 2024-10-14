package com.qticket.coupon.presentation;

import com.qticket.common.login.CurrentUser;
import com.qticket.common.login.Login;
import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByCustomerRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetIssuedCouponResponseDto;
import com.qticket.coupon.application.coupon.service.CouponService;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CouponCreateResponseDto create(@Login CurrentUser currentUser, @RequestBody CouponCreateRequestDto couponCreateRequestDto) {
        return couponService.create(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), couponCreateRequestDto);
    }

    @PostMapping("/admin-issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issueByAdmin(@Login CurrentUser currentUser, @RequestBody IssueByAdminRequestDto issueByAdminRequestDto) {
        couponService.sendIssueByAdminMessage(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), issueByAdminRequestDto);
    }

    @PostMapping("/customer-issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issueByCustomer(@Login CurrentUser currentUser, @RequestBody IssueByCustomerRequestDto issueByCustomerRequestDto) {
        couponService.sendIssueByCustomer(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), issueByCustomerRequestDto);
    }

    @GetMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public GetCouponResponseDto getCoupon(@Login CurrentUser currentUser, @PathVariable UUID couponId) {
        return couponService.getCoupon(currentUser.getCurrentUserRole(), couponId);
    }

    @DeleteMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public CouponDeleteResponseDto delete(@Login CurrentUser currentUser, @PathVariable UUID couponId) {
        return couponService.delete(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), couponId);
    }

    @GetMapping("/{couponId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public GetIssuedCouponResponseDto getIssuedCoupon(@Login CurrentUser currentUser,
                                                      @PathVariable UUID couponId,
                                                      @PathVariable Long userId) {
        return couponService.getIssuedCoupon(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), couponId, userId);
    }

    @PostMapping("/{couponId}/apply")
    @ResponseStatus(HttpStatus.OK)
    public void apply(@Login CurrentUser currentUser, @PathVariable UUID couponId) {
        couponService.apply(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), couponId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<GetCouponResponseDto> getCoupons(
            @Login CurrentUser currentUser,
            @RequestParam @Nullable String search,
            @RequestParam @Nullable String isDeleted,
            @RequestParam @Nullable String status,
            @RequestParam @Nullable CouponTarget target,
            Pageable pageable
    ) {
        return couponService.getCoupons(currentUser.getCurrentUserRole(), search, isDeleted, target, status, pageable);
    }


    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetIssuedCouponResponseDto> getIssuedCoupons(
            @Login CurrentUser currentUser,
            @PathVariable Long userId,
            @RequestParam @Nullable String isDeleted,
            @RequestParam @Nullable CouponTarget target,
            @RequestParam @Nullable String usable,
            @RequestParam @Nullable UUID eventId,
            Pageable pageable
    ){
        return couponService.getIssuedCoupons(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), userId, isDeleted, target, usable, eventId, pageable);
    }
}
