package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class DiscountRateExceedsLimitException extends QueueTicketException {
    public DiscountRateExceedsLimitException() {
        super(CouponErrorCode.DISCOUNT_RATE_EXCEEDS_LIMIT);
    }
}
