package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class AlreadyIssuedUserException extends QueueTicketException {
    public AlreadyIssuedUserException() {
        super(CouponErrorCode.ALREADY_ISSUED_USER);
    }
}
