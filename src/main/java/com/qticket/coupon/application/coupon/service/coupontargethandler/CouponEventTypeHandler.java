package com.qticket.coupon.application.coupon.service.coupontargethandler;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetIssuedCouponResponseDto;
import com.qticket.coupon.application.coupon.exception.InvalidEventForCouponException;
import com.qticket.coupon.application.eventclient.dto.response.GetEventResponseDto;
import com.qticket.coupon.application.eventclient.service.EventServiceClient;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import com.qticket.coupon.domain.couponevent.repository.CouponEventRepository;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.infrastructure.eventclient.exception.EventAlreadyFinishedException;
import com.qticket.coupon.infrastructure.eventclient.exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private GetEventResponseDto getEventById(UUID eventId) {
        ResponseDto<GetEventResponseDto> response = eventServiceClient.getEvent(eventId);
        if (response.getStatus().equals("error")) {
            throw new EventNotFoundException();
        }
        return response.getData();
    }

    private void validateEventDate(GetEventResponseDto getOneResponseDto) {
        if (LocalDateTime.now().isBefore(getOneResponseDto.getConcertStartTime())) {
            throw new EventAlreadyFinishedException();
        }
    }

    @Override
    public CouponTarget getType() {
        return CouponTarget.EVENT;
    }

    @Override
    @Transactional(readOnly = true)
    public GetCouponResponseDto getCouponResponseDto(Coupon coupon) {
        List<CouponEvent> allByCoupon = couponEventRepository.findAllByCoupon(coupon);
        List<GetCouponResponseDto.ConcertDto> concertDtoList = getConcertDtoList(allByCoupon);

        return new GetCouponResponseDto(coupon, concertDtoList);
    }

    private List<GetCouponResponseDto.ConcertDto> getConcertDtoList(List<CouponEvent> allByCoupon) {
        ArrayList<GetCouponResponseDto.ConcertDto> concertDtoList = new ArrayList<>();
        for (CouponEvent couponEvent : allByCoupon) {
            try {
                UUID eventId = couponEvent.getEventId();
                GetEventResponseDto eventById = getEventById(eventId);
                GetCouponResponseDto.ConcertDto concertDto = new GetCouponResponseDto.ConcertDto(eventById.getConcertId(), eventById.getConcertTitle());
                concertDtoList.add(concertDto);
            } catch (Exception e){

            }
        }
        return concertDtoList;
    }

    @Override
    public GetIssuedCouponResponseDto getIssuedCouponResponseDto(Coupon coupon, CouponUser couponUser) {
        List<CouponEvent> allByCoupon = couponEventRepository.findAllByCoupon(coupon);
        List<GetCouponResponseDto.ConcertDto> concertDtoList = getConcertDtoList(allByCoupon);
        return new GetIssuedCouponResponseDto(coupon, concertDtoList, couponUser);
    }

    @Override
    public void validate(Long userId, Coupon coupon, UUID eventId) {
        couponEventRepository.findByCouponAndEventId(coupon, eventId).orElseThrow(InvalidEventForCouponException::new);
    }
}
