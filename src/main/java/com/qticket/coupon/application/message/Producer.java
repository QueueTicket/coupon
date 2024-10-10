package com.qticket.coupon.application.message;

import com.qticket.coupon.application.coupon.dto.request.IssueByAdminRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueByCustomerRequestDto;

public interface Producer {

    void sendIssueByAdminMessage(Long userId, String userRole, IssueByAdminRequestDto issueByAdminRequestDto);

    void sendIssueByCustomerMessage(Long userId, String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto);

}
