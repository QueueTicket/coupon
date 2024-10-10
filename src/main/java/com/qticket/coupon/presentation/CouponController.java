package com.qticket.coupon.presentation;

import com.qticket.common.login.CurrentUser;
import com.qticket.common.login.Login;
import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByCustomerRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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


}
