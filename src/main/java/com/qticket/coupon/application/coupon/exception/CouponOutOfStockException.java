package com.qticket.coupon.application.coupon.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.domain.coupon.exception.CouponErrorCode;

public class CouponOutOfStockException extends QueueTicketException {
    public CouponOutOfStockException() {
        super(CouponErrorCode.COUPON_OUT_OF_STOCK);
    }
}
