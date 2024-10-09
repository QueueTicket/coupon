package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponDeleteRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponUpdateRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponUpdateResponseDto;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistry;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistryImpl;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CouponServiceUnitTest {

    @Test
    void 쿠폰_생성_성공_단위_테스트() {

        // given
        CouponRepository couponRepository = FakeCouponRepository.getInstance();
        CouponTypeRegistry couponTypeRegistry = new FakeCouponTypeRegistry();
        CouponService couponService = new CouponServiceImpl(couponRepository, couponTypeRegistry);

        CouponCreateRequestDto createRequest = new CouponCreateRequestDto();
        createRequest.setName("테스트 쿠폰");
        createRequest.setDiscountPolicy(DiscountPolicy.FIXED);
        createRequest.setDiscountAmount(20000);
        createRequest.setTarget(CouponTarget.ALL);
        createRequest.setExpirationDate(LocalDateTime.of(2024,8,20,16,30));
        createRequest.setStartDate(LocalDateTime.of(2024,7,20,16,30));
        createRequest.setMinSpendAmount(10000);
        createRequest.setMaxQuantity(100000);
        createRequest.setUsageLimit(5);

        // when
        CouponCreateResponseDto response = couponService.create(1L, "ADMIN", createRequest);

        // then
        Assertions.assertInstanceOf(UUID.class, response.getCouponId());
    }

    @Test
    void 쿠폰_삭제_성공_단위_테스트() {
        // given
        CouponRepository couponRepository = FakeCouponRepository.getInstance();
        CouponTypeRegistry couponTypeRegistry = new FakeCouponTypeRegistry();
        CouponService couponService = new CouponServiceImpl(couponRepository, couponTypeRegistry);

        CouponCreateRequestDto createRequest = new CouponCreateRequestDto();
        createRequest.setName("테스트 쿠폰");
        createRequest.setDiscountPolicy(DiscountPolicy.FIXED);
        createRequest.setDiscountAmount(20000);
        createRequest.setTarget(CouponTarget.ALL);
        createRequest.setExpirationDate(LocalDateTime.of(2024,8,20,16,30));
        createRequest.setStartDate(LocalDateTime.of(2024,7,20,16,30));
        createRequest.setMinSpendAmount(10000);
        createRequest.setMaxQuantity(100000);
        createRequest.setUsageLimit(5);

        CouponCreateResponseDto couponCreateResponse = couponService.create(1L, "ADMIN", createRequest);
        CouponDeleteRequestDto couponDeleteRequestDto = new CouponDeleteRequestDto(couponCreateResponse.getCouponId());
        System.out.println("couponDeleteRequestDto = " + couponDeleteRequestDto.getCouponId());
        // when
        CouponDeleteResponseDto couponDeleteResponse = couponService.delete(1L, "ADMIN", couponDeleteRequestDto);
        // then
        assertThat(couponCreateResponse.getCouponId()).isEqualTo(couponDeleteResponse.getCouponId());
    }

//    @Test
//    void 쿠폰_업데이트_성공_단위_테스트() {
//        // given
//
//        new Coupon
//        CouponService couponService = new CouponServiceImpl();
//
//        CouponCreateRequestDto createRequest = new CouponCreateRequestDto();
//        createRequest.setName("테스트 쿠폰");
//        createRequest.setDiscountPolicy(DiscountPolicy.FIXED);
//        createRequest.setDiscountAmount(20000);
//        createRequest.setTarget(CouponTarget.ALL);
//        createRequest.setExpirationDate(LocalDateTime.of(2024,8,20,16,30));
//        createRequest.setStartDate(LocalDateTime.of(2024,7,20,16,30));
//        createRequest.setMinSpendAmount(10000);
//        createRequest.setMaxQuantity(100000);
//        createRequest.setUsageLimit(5);
//
//        CouponCreateResponseDto couponCreateResponse = couponService.create(1L, "ADMIN", createRequest);
//
//        CouponUpdateRequestDto couponUpdateRequestDto = new CouponUpdateRequestDto();
//        couponUpdateRequestDto.setName("수정 테스트 쿠폰");
//        couponUpdateRequestDto.setDiscountPolicy(DiscountPolicy.FIXED);
//        couponUpdateRequestDto.setDiscountAmount(20000);
//        couponUpdateRequestDto.setTarget(CouponTarget.ALL);
//        couponUpdateRequestDto.setExpirationDate(LocalDateTime.of(2024,8,20,16,30));
//        couponUpdateRequestDto.setStartDate(LocalDateTime.of(2024,7,20,16,30));
//        couponUpdateRequestDto.setMinSpendAmount(10000);
//        couponUpdateRequestDto.setMaxQuantity(100000);
//        couponUpdateRequestDto.setUsageLimit(5);
//
//        // when
//        CouponUpdateResponseDto couponUpdateResponse = couponService.update(1L, "ADMIN", couponCreateResponse.getCouponId(), couponUpdateRequestDto);
//
//        // then
//        assertThat(couponUpdateRequestDto.getName()).isEqualTo(couponUpdateResponse.getName());
//    }
}
