package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class UnauthorizedAccessException extends QueueTicketException {
    public UnauthorizedAccessException() {
        super(CouponErrorCode.UNAUTHORIZED_ACCESS);
    }
}
