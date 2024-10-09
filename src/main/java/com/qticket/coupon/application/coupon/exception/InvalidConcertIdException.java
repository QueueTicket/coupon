package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class InvalidConcertIdException extends QueueTicketException {
    public InvalidConcertIdException() {
        super(CouponErrorCode.INVALID_CONCERT_ID);
    }
}
