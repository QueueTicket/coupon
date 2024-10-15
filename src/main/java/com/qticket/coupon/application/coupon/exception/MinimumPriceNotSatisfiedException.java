package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class MinimumPriceNotSatisfiedException extends QueueTicketException {
    public MinimumPriceNotSatisfiedException() {
        super(CouponErrorCode.MINIMUM_PRICE_NOT_SATISFIED_EXCEPTION);
    }
}
