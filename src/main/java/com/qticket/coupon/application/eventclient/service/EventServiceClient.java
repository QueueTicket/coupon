package com.qticket.coupon.application.eventclient.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.eventclient.dto.response.GetEventResponseDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface EventServiceClient {
    ResponseDto<GetEventResponseDto> getEvent(UUID eventId);
}
