package com.qticket.coupon.application.eventclient.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.eventclient.dto.response.GetOneResponseDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface EventServiceClient {
    ResponseDto<GetOneResponseDto> getOne(UUID eventId);
}
