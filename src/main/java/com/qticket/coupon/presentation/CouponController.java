package com.qticket.coupon.presentation;

import com.qticket.common.login.CurrentUser;
import com.qticket.common.login.Login;
import com.qticket.coupon.application.coupon.dto.request.*;
import com.qticket.coupon.application.coupon.dto.response.*;
import com.qticket.coupon.application.coupon.service.CouponService;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CouponCreateResponseDto create(@Login CurrentUser currentUser,@Valid @RequestBody CouponCreateRequestDto couponCreateRequestDto) {
        return couponService.create(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), couponCreateRequestDto);
    }

    @PostMapping("/admin-issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issueByAdmin(@Login CurrentUser currentUser, @Valid @RequestBody IssueByAdminRequestDto issueByAdminRequestDto) {
        couponService.sendIssueByAdminMessage(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), issueByAdminRequestDto);
    }

    @PostMapping("/customer-issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issueByCustomer(@Login CurrentUser currentUser, @Valid @RequestBody IssueByCustomerRequestDto issueByCustomerRequestDto) {
        couponService.sendIssueByCustomer(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), issueByCustomerRequestDto);
    }

    @GetMapping("/{couponId}")
    @ResponseStatus(HttpStatus.OK)
    public GetCouponResponseDto getCoupon(@Login CurrentUser currentUser, @PathVariable UUID couponId) {
        // todo
        // coupon/couponId 가 permitAll
        // 1번 : 로그인 안한 유저: 게이트웨이 -> 로그인 필터를 거치지 않음
        // 2번 : 로그인 한 유저(관리자) : 게이트웨이 -> 로그인 필터를 거침

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

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetCouponByAdminResponseDto> getCouponsByAdmin(
            @Login CurrentUser currentUser,
            @RequestParam @Nullable Long userId,
            @RequestParam @Nullable String isDeleted,
            @RequestParam @Nullable CouponTarget target,
            @RequestParam @Nullable String status,
            Pageable pageable
    ) {
        return couponService.getCouponsByAdmin(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), userId, isDeleted, target, status, pageable);

    }

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public CouponValidateResponseDto validate(@Valid @RequestBody CouponValidateRequestDto couponValidateRequestDto) {
        return couponService.validate(couponValidateRequestDto.getUserId(), couponValidateRequestDto.getCouponId(), couponValidateRequestDto.getEventId(), couponValidateRequestDto.getPrice());
    }
}
