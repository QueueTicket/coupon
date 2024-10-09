package com.qticket.coupon.application.eventclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetOneResponseDto {
    private UUID concertId;
    private String concertName;
    private LocalDateTime concertStartTime;
    private int playTime;
    private String description;

}

