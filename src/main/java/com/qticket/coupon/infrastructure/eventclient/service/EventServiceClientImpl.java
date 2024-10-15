package com.qticket.coupon.infrastructure.eventclient.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.eventclient.dto.response.GetEventResponseDto;
import com.qticket.coupon.application.eventclient.service.EventServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceClientImpl implements EventServiceClient {

    private final EventServiceFeignClient eventServiceFeignClient;

    @Override
    public ResponseDto<GetEventResponseDto> getEvent(UUID eventId) {
        return eventServiceFeignClient.getEvent(eventId);


    }
}
