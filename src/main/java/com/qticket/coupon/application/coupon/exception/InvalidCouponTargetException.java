package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class InvalidCouponTargetException extends QueueTicketException {
    public InvalidCouponTargetException() {
        super(CouponErrorCode.INVALID_COUPON_TARGET);
    }
}