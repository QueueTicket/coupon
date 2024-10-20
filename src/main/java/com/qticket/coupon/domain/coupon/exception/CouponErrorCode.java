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
    COUPON_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "쿠폰이 이미 모두 발급 되었습니다."),
    ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 쿠폰입니다."),
    USER_NOT_ISSUED_COUPON(HttpStatus.BAD_REQUEST, "유저가 해당 쿠폰을 발급 받지 않았습니다."),
    USAGE_LIMIT_EXCEED(HttpStatus.BAD_REQUEST, "쿠폰 사용 횟수를 초과하였습니다."),
    COUPON_ALREADY_EXPIRED(HttpStatus.BAD_REQUEST, "이미 만료된 쿠폰입니다."),
    MINIMUM_PRICE_NOT_SATISFIED_EXCEPTION(HttpStatus.BAD_REQUEST, "결제 금액이 쿠폰의 최소 주문 가격보다 작습니다."),
    INVALID_EVENT_FOR_COUPON(HttpStatus.BAD_REQUEST, "요청 받은 공연에 적용할 수 없는 쿠폰입니다."),
    DISCOUNT_RATE_EXCEEDS_LIMIT(HttpStatus.BAD_REQUEST, "할인율이 100%를 초과할 수 없습니다."),
    DISCOUNT_AMOUNT_NOT_MATCH_MAX_DISCOUNT_PRICE(HttpStatus.BAD_REQUEST, "할인 금액과 최대한 할인 금액은 일치해야 합니다."),
    COUPON_TIME_INVALID(HttpStatus.BAD_REQUEST, "쿠폰 시작 시간과 만료 시간이 적절하게 설정되지 않았습니다."),
    COUPON_CANNOT_ISSUE(HttpStatus.BAD_REQUEST, "쿠폰을 발급 받을 수 없습니다."),
    ADMIN_ISSUE_ONLY_UNLIMITED_COUPON(HttpStatus.BAD_REQUEST, "관리자는 수량이 무제한인 쿠폰만 발급 유저에게 발급할 수 있습니다."),
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
