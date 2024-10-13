package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.cache.CacheRepository;
import com.qticket.coupon.application.coupon.dto.request.*;
import com.qticket.coupon.application.coupon.dto.response.CouponCreateResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponDeleteResponseDto;
import com.qticket.coupon.application.coupon.dto.response.CouponUpdateResponseDto;
import com.qticket.coupon.application.coupon.exception.AlreadyIssuedUserException;
import com.qticket.coupon.application.coupon.exception.CouponNotFoundException;
import com.qticket.coupon.application.coupon.exception.CouponOutOfStockException;
import com.qticket.coupon.application.coupon.exception.UnauthorizedAccessException;
import com.qticket.coupon.application.message.Producer;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistry;
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
    private final CacheRepository cacheRepository;

    @Override
    @Transactional
    public CouponCreateResponseDto create(Long userId, String userRole, CouponCreateRequestDto requestDto) {

        isAdmin(userRole);

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(requestDto.getTarget());
        Coupon coupon = couponHandler.create(requestDto);

        if (coupon.hasMaxQuantity()) {
            cacheRepository.save(coupon);
        }

        return new CouponCreateResponseDto(coupon.getId());
    }

    private void isAdmin(String userRole) {
        if (!"ADMIN".equals(userRole)) {
            throw new UnauthorizedAccessException();
        }
    }

    @Override
    @Transactional
    public CouponDeleteResponseDto delete(Long userId, String userRole, CouponDeleteRequestDto couponDeleteRequestDto) {
        isAdmin(userRole);
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
        isAdmin(userRole);
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
    public void sendIssueByAdminMessage(Long userId, String userRole, IssueByAdminRequestDto issueByAdminRequestDto) {
        isAdmin(userRole);
        producer.sendIssueByAdminMessage(userId, userRole, issueByAdminRequestDto);

    }

    @Override
    @Transactional
    public void issueByAdmin(IssueByAdminRequestDto issueByAdminRequestDto) {
        UUID couponId = issueByAdminRequestDto.getCouponId();
        Long userId = issueByAdminRequestDto.getUserId();
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

    @Override
    public void sendIssueByCustomer(Long userId, String userRole, IssueByCustomerRequestDto issueByCustomerRequestDto) {
        UUID couponId = issueByCustomerRequestDto.getCouponId();

        Coupon coupon = getCouponById(couponId);

        validate(userRole, couponId, userId);

        if (coupon.hasMaxQuantity()) {
            Long stockQuantity = cacheRepository.decreaseCouponQuantity(couponId);
            validateCouponQuantity(stockQuantity, couponId);
            cacheRepository.issueCoupon(couponId, userId);
        }

        producer.sendIssueByCustomerMessage(userId, userRole, issueByCustomerRequestDto);
    }

    @Override
    @Transactional
    public void IssueByCustomer(Long userId, IssueByCustomerRequestDto issueByCustomerRequestDto) {
        UUID couponId = issueByCustomerRequestDto.getCouponId();
        Coupon coupon = getCouponById(couponId);

        List<CouponUser> couponUsers = couponUserRepository.findAllByUserIdAndCoupon(userId, coupon);
        validateAlreadyIssued(couponUsers);

        CouponUser couponUser = CouponUser.create(userId, coupon);
        couponUserRepository.save(couponUser);

        coupon.issue();
        couponRepository.save(coupon);

    }

    private void validate(String userRole, UUID couponId, Long userId) {
        isCustomer(userRole);
        validateAlreadyIssued(couponId, userId);
    }
    private void validateAlreadyIssued(UUID couponId, Long userId) {
        if (cacheRepository.isExistByCouponIdAndUserId(couponId, userId)) {
            throw new AlreadyIssuedUserException();
        }
    }

    private void validateCouponQuantity(Long couponQuantity, UUID couponId) {
        if (couponQuantity != null && couponQuantity < 0) {
            cacheRepository.increaseCouponQuantity(couponId);
            throw new CouponOutOfStockException();
        }
    }

    private void isCustomer(String userRole) {
        if (!userRole.equals("CUSTOMER")) {
            throw new UnauthorizedAccessException();
        }
    }
}
