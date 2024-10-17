package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class CouponTimeInvalidException extends QueueTicketException {
    public CouponTimeInvalidException() {
        super(CouponErrorCode.COUPON_TIME_INVALID);
    }
}
