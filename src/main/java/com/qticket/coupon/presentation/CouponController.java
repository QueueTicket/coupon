package com.qticket.coupon.presentation;

import com.qticket.common.login.CurrentUser;
import com.qticket.common.login.Login;
import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    /*
     * TODO
     *  @Login CurrentUser currentUser 후에 추가
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CouponCreateResponseDto create(@Login CurrentUser currentUser, @RequestBody CouponCreateRequestDto couponCreateRequestDto) {
        return couponService.create(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), couponCreateRequestDto);
    }

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void issue(@Login CurrentUser currentUser, @RequestBody IssueRequestDto issueRequestDto) {
        couponService.sendIssueMessage(currentUser.getCurrentUserId(), currentUser.getCurrentUserRole(), issueRequestDto);
    }
}
