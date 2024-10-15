package com.qticket.coupon.infrastructure.eventclient.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.eventclient.dto.response.GetEventResponseDto;
import com.qticket.coupon.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="concert-server", configuration = FeignConfig.class)
public interface EventServiceFeignClient {

    @GetMapping(path = "/concerts/{concertId}",  produces = "application/json")
    ResponseDto<GetEventResponseDto> getEvent(@PathVariable UUID concertId);
}
