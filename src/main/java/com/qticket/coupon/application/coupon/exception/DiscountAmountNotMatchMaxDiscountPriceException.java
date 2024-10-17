package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class DiscountAmountNotMatchMaxDiscountPriceException extends QueueTicketException {
    public DiscountAmountNotMatchMaxDiscountPriceException() {
        super(CouponErrorCode.DISCOUNT_AMOUNT_NOT_MATCH_MAX_DISCOUNT_PRICE);
    }
}
