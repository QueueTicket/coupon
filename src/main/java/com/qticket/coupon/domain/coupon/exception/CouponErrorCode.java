package com.qticket.coupon.domain.coupon.exception;

import com.qticket.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {

    CONCERT_ID_NOT_REQUIRED(HttpStatus.BAD_REQUEST, "전체 적용 쿠폰에는 concertId를 입력받지 않습니다."),
    INVALID_CONCERT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 concertId 입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    INVALID_COUPON_TARGET(HttpStatus.BAD_REQUEST, "유효하지 않은 쿠폰 타입입니다."),
    ALREADY_ISSUED_USER(HttpStatus.BAD_REQUEST, "이미 쿠폰을 발급 받은 유저입니다."),
    COUPON_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "쿠폰이 이미 모두 발급 되었습니다.")
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
