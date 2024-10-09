package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class CouponNotFoundException extends QueueTicketException {
    public CouponNotFoundException() {
        super(CouponErrorCode.NOT_FOUND);
    }
}
