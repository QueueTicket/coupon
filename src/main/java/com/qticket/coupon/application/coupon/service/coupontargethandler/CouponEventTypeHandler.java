package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.eventclient.dto.response.GetOneResponseDto;
import com.qticket.coupon.application.eventclient.service.EventServiceClient;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import com.qticket.coupon.domain.couponevent.repository.CouponEventRepository;
import com.qticket.coupon.infrastructure.eventclient.exception.EventAlreadyFinishedException;
import com.qticket.coupon.infrastructure.eventclient.exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponEventTypeHandler implements CouponTypeHandler {

    private final CouponRepository couponRepository;
    private final CouponEventRepository couponEventRepository;
    private final EventServiceClient eventServiceClient;

    @Override
    @Transactional
    public Coupon create(CouponCreateRequestDto couponCreateRequestDto) {

        List<UUID> eventIds = couponCreateRequestDto.getEventId();

        validateEvent(eventIds);

        Coupon coupon = CouponTypeHandler.toEntity(couponCreateRequestDto);

        List<CouponEvent> couponEvents = eventIds.stream()
                .map(eventId -> CouponEvent.create(eventId, coupon))
                .toList();

        couponEventRepository.saveAll(couponEvents);
        return couponRepository.save(coupon);
    }


    private void validateEvent(List<UUID> eventIds) {
        eventIds.stream()
                .map(this::getEventById)
                .forEach(this::validateEventDate);
    }

    private GetOneResponseDto getEventById(UUID eventId) {
        ResponseDto<GetOneResponseDto> response = eventServiceClient.getOne(eventId);
        if (response.getStatus().equals("error")) {
            throw new EventNotFoundException();
        }
        return response.getData();
    }

    private void validateEventDate(GetOneResponseDto getOneResponseDto) {
        if (LocalDateTime.now().isAfter(getOneResponseDto.getConcertStartTime())) {
            throw new EventAlreadyFinishedException();
        }
    }

    @Override
    public CouponTarget getType() {
        return CouponTarget.EVENT;
    }
}
