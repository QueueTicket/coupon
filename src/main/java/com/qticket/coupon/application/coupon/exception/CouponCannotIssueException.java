package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class CouponCannotIssueException extends QueueTicketException {
    public CouponCannotIssueException() {
        super(CouponErrorCode.COUPON_CANNOT_ISSUE);
    }
}
