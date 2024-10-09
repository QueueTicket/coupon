package com.qticket.coupon.infrastructure.eventclient.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.eventclient.dto.response.GetOneResponseDto;
import com.qticket.coupon.application.eventclient.service.EventServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TempEventServiceClientImpl implements EventServiceClient {
    @Override
    public ResponseDto<GetOneResponseDto> getOne(UUID eventId) {
        GetOneResponseDto getOneResponseDto = new GetOneResponseDto(eventId, "아이유 콘서트", LocalDateTime.now(), 200, "20주년 아이유 콘서트");
        return ResponseDto.success(HttpStatus.OK.name(), getOneResponseDto);
    }
}
