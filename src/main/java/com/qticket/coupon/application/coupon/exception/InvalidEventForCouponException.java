package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class InvalidEventForCouponException extends QueueTicketException {
    public InvalidEventForCouponException() {
        super(CouponErrorCode.INVALID_EVENT_FOR_COUPON);
    }
}
