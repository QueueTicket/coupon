package com.qticket.coupon.application.eventclient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEventResponseDto implements Serializable {
    private UUID concertId;
    private UUID venueId;
    private String concertTitle;
    private LocalDateTime concertStartTime;
    private Integer playtime;
    private String description;
    private List<PriceResponse> prices;

    @Getter
    public static class PriceResponse{
        private Integer price;
        private SeatGrade seatGrade;
    }

    enum SeatGrade {
        R("R석"),
        S("S석"),
        A("A석"),
        B("B석");

        final String name;

        SeatGrade(String name) {
            this.name = name;
        }
    }
}
