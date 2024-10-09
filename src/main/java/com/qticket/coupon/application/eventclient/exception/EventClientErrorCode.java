package com.qticket.coupon.application.eventclient.exception;

import com.qticket.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum EventClientErrorCode implements ErrorCode {

    CONCERT_ALREADY_FINISHED(HttpStatus.BAD_REQUEST, "이미 종료된 콘서트입니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "콘서트를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
