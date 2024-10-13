package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class UsageLimitExceedException extends QueueTicketException {
    public UsageLimitExceedException() {
        super(CouponErrorCode.USAGE_LIMIT_EXCEED);
    }
}
