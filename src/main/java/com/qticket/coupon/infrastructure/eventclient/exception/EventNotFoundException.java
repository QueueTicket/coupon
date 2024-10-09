package com.qticket.coupon.infrastructure.eventclient.exception;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.coupon.application.eventclient.exception.EventClientErrorCode;

public class EventNotFoundException extends QueueTicketException {
    public EventNotFoundException() {
        super(EventClientErrorCode.NOT_FOUND);
    }
}
