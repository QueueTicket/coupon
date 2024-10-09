package com.qticket.coupon.infrastructure.eventclient.exception;

import com.qticket.common.exception.ErrorCode;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.application.eventclient.exception.EventClientErrorCode;

public class EventAlreadyFinishedException extends QueueTicketException {
    public EventAlreadyFinishedException() {
        super(EventClientErrorCode.CONCERT_ALREADY_FINISHED);
    }
}
