package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class ConcertIdNotRequiredException extends QueueTicketException {
    public ConcertIdNotRequiredException() {
        super(CouponErrorCode.CONCERT_ID_NOT_REQUIRED);
    }
}
