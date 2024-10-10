package com.qticket.coupon.application.message;

import com.qticket.coupon.application.coupon.dto.request.IssueByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByCustomerRequestDto;
import org.springframework.messaging.handler.annotation.Header;

public interface Consumer {

    void listenIssueByAdminMessage(@Header("X-USER-ID") String userId, @Header("X-USER-Role") String userRole, IssueByAdminRequestDto issueByAdminRequestDto);

    void listenIssueByCustomerMessage(@Header("X-USER-ID") String userId, @Header("X-USER-Role") String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto);
}
