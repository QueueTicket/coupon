package com.qticket.coupon.application.coupon.service.couponmessage;

import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;
import org.springframework.messaging.handler.annotation.Header;

public interface Consumer {

    void listenIssueCouponMessage(@Header("X-USER-ID") String userId, @Header("X-USER-Role") String userRole, IssueRequestDto issueRequestDto);
}
