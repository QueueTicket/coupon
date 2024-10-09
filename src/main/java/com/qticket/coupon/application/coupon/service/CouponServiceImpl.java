package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.coupon.dto.request.CouponCreateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponDeleteRequestDto;
import com.qticket.coupon.application.coupon.dto.request.CouponUpdateRequestDto;
import com.qticket.coupon.application.coupon.dto.request.IssueRequestDto;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponUpdateResponseDto;
import com.qticket.coupon.application.coupon.exception.AlreadyIssuedUserException;
import com.qticket.coupon.application.coupon.exception.CouponNotFoundException;
import com.qticket.coupon.application.coupon.exception.UnauthorizedAccessException;
import com.qticket.coupon.application.coupon.service.couponmessage.Producer;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistry;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistryImpl;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponTypeRegistry couponTypeRegistry;
    private final CouponUserRepository couponUserRepository;
    private final Producer producer;

    @Override
    @Transactional
    public CouponCreateResponseDto create(Long userId, String userRole, CouponCreateRequestDto requestDto) {

        isAdminUser(userRole);

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(requestDto.getTarget());
        Coupon coupon = couponHandler.create(requestDto);

        return new CouponCreateResponseDto(coupon.getId());

    }

    private void isAdminUser(String userRole) {
        if (!"ADMIN".equals(userRole)) {
            throw new UnauthorizedAccessException();
        }
    }

    @Override
    @Transactional
    public CouponDeleteResponseDto delete(Long userId, String userRole, CouponDeleteRequestDto couponDeleteRequestDto) {
        isAdminUser(userRole);
        Coupon coupon = getCouponById(couponDeleteRequestDto.getCouponId());
        coupon.softDelete(String.valueOf(userId));
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponDeleteResponseDto(savedCoupon.getId());
    }

    private Coupon getCouponById(UUID couponId) {

        return couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);
    }

    @Override
    @Transactional
    public CouponUpdateResponseDto update(Long userId, String userRole, UUID couponId, CouponUpdateRequestDto couponUpdateRequestDto) {
        isAdminUser(userRole);
        Coupon coupon = getCouponById(couponId);
        update(coupon, couponUpdateRequestDto);
        couponRepository.save(coupon);
        return new CouponUpdateResponseDto(coupon);
    }

    private void update(Coupon coupon, CouponUpdateRequestDto updateRequestDto) {
        coupon.update(
                updateRequestDto.getName(),
                updateRequestDto.getDiscountAmount(),
                updateRequestDto.getDiscountPolicy(),
                updateRequestDto.getTarget(),
                updateRequestDto.getStartDate(),
                updateRequestDto.getExpirationDate(),
                updateRequestDto.getMinSpendAmount(),
                updateRequestDto.getUsageLimit(),
                updateRequestDto.getMaxQuantity());
    }

    @Override
    public void sendIssueMessage(Long userId, String userRole, IssueRequestDto issueRequestDto) {
        isAdminUser(userRole);
        producer.sendIssueCouponMessage(userId, userRole, issueRequestDto);

    }

    @Override
    @Transactional
    public void issue(IssueRequestDto issueRequestDto) {
        UUID couponId = issueRequestDto.getCouponId();
        Long userId = issueRequestDto.getUserId();
        Coupon coupon = getCouponById(couponId);

        List<CouponUser> couponUsers = couponUserRepository.findAllByUserIdAndCoupon(userId, coupon);
        validateAlreadyIssued(couponUsers);

        CouponUser couponUser = CouponUser.create(userId, coupon);
        couponUserRepository.save(couponUser);

        coupon.issue();
        couponRepository.save(coupon);
    }

    public void validateAlreadyIssued(List<CouponUser> couponUsers) {
        if (!couponUsers.isEmpty()) {
            throw new AlreadyIssuedUserException();
        }
    }

}
