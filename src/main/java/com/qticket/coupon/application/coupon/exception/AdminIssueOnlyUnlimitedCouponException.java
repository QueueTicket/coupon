package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class AdminIssueOnlyUnlimitedCouponException extends QueueTicketException {
    public AdminIssueOnlyUnlimitedCouponException() {
        super(CouponErrorCode.ADMIN_ISSUE_ONLY_UNLIMITED_COUPON);
    }
}