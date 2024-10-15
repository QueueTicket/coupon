package com.qticket.coupon.application.coupon.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.eventclient.service.EventServiceClient;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class FakeEventServiceClientImpl implements EventServiceClient {
    @Override
    public ResponseDto<GetOneResponseDto> getOne(UUID eventId) {
        GetOneResponseDto getOneResponseDto = new GetOneResponseDto(eventId, "아이유 콘서트", LocalDateTime.now(), 200, "10주년 콘서트");
        return ResponseDto.success(HttpStatus.OK.name(), getOneResponseDto);
    }
}
