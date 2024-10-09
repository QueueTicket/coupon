package com.qticket.coupon.application.coupon.service.couponmessage;

import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;

public interface Producer {

    void sendIssueCouponMessage(Long userId, String userRole, IssueRequestDto issueRequestDto);
}
