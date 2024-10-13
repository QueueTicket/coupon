package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class CouponAlreadyExpiredException extends QueueTicketException {
    public CouponAlreadyExpiredException() {
        super(CouponErrorCode.COUPON_ALREADY_EXPIRED);
    }
}
