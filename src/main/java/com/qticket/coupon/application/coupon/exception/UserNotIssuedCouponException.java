package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class UserNotIssuedCouponException extends QueueTicketException {
    public UserNotIssuedCouponException() {
        super(CouponErrorCode.USER_NOT_ISSUED_COUPON);
    }
}
