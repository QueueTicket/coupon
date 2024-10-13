package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class CouponAlreadyDeletedException extends QueueTicketException {
    public CouponAlreadyDeletedException() {
        super(CouponErrorCode.ALREADY_DELETED);
    }
}
