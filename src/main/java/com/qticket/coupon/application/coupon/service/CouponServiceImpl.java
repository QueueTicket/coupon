package com.qticket.coupon.application.coupon.service;

import com.qticket.common.dto.ResponseDto;
import com.qticket.coupon.application.cache.CacheRepository;
import com.qticket.coupon.application.coupon.dto.request.*;
import com.qticket.coupon.application.coupon.dto.response.*;
import com.qticket.coupon.application.coupon.exception.*;
import com.qticket.coupon.application.eventclient.dto.response.GetOneResponseDto;
import com.qticket.coupon.application.eventclient.service.EventServiceClient;
import com.qticket.coupon.application.message.Producer;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistry;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

        validateIsAdmin(userRole);

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(requestDto.getTarget());
        Coupon coupon = couponHandler.create(requestDto);

        if (coupon.hasMaxQuantity()) {
            cacheRepository.save(coupon);
        }

        return new CouponCreateResponseDto(coupon.getId());
    }

    private void validateIsAdmin(String userRole) {
        if (!(isAdmin(userRole))) {
            throw new UnauthorizedAccessException();
        }
    }
    private boolean isAdmin(String userRole) {
        return "ADMIN".equals(userRole);
    }

    @Override
    @Transactional
    public CouponDeleteResponseDto delete(Long userId, String userRole, UUID couponId) {
        validateIsAdmin(userRole);
        Coupon coupon = getCouponById(couponId);

        validateAlreadyDeleted(coupon);

        coupon.softDelete(String.valueOf(userId));
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponDeleteResponseDto(savedCoupon.getId());
    }

    private void validateAlreadyDeleted(Coupon coupon) {
        if (coupon.isDelete()) {
            throw new CouponAlreadyDeletedException();
        }
    }

    private Coupon getCouponById(UUID couponId) {

        return couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);
    }

    @Override
    @Transactional
    public CouponUpdateResponseDto update(Long userId, String userRole, UUID couponId, CouponUpdateRequestDto couponUpdateRequestDto) {
        validateIsAdmin(userRole);
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
        validateIsAdmin(userRole);
        producer.sendIssueByAdminMessage(userId, userRole, issueByAdminRequestDto);

    }

    @Override
    @Transactional
    public void issueByAdmin(IssueByAdminRequestDto issueByAdminRequestDto) {
        UUID couponId = issueByAdminRequestDto.getCouponId();
        Long userId = issueByAdminRequestDto.getUserId();
        Coupon coupon = getCouponById(couponId);

        Optional<CouponUser> savedCouponUser = couponUserRepository.findByUserIdAndCoupon(userId, coupon);
        validateAlreadyIssued(savedCouponUser);

        CouponUser couponUser = CouponUser.create(userId, coupon);
        couponUserRepository.save(couponUser);

        coupon.issue();
        couponRepository.save(coupon);
    }


    public void validateAlreadyIssued(Optional<CouponUser> savedCouponUser) {
        if (savedCouponUser.isPresent()) {
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
    public void issueByCustomer(Long userId, IssueByCustomerRequestDto issueByCustomerRequestDto) {
        UUID couponId = issueByCustomerRequestDto.getCouponId();
        Coupon coupon = getCouponById(couponId);

        Optional<CouponUser> savedCouponUser = couponUserRepository.findByUserIdAndCoupon(userId, coupon);
        validateAlreadyIssued(savedCouponUser);

        CouponUser couponUser = CouponUser.create(userId, coupon);
        couponUserRepository.save(couponUser);

        coupon.issue();
        couponRepository.save(coupon);

    }

    private void validate(String userRole, UUID couponId, Long userId) {
        validateIsCustomer(userRole);
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

    private void validateIsCustomer(String userRole) {
        if (!isCustomer(userRole)) {
            throw new UnauthorizedAccessException();
        }
    }
    private boolean isCustomer(String userRole) {
        return userRole.equals("CUSTOMER");
    }

    @Override
    @Transactional(readOnly = true)
    public GetCouponResponseDto getCoupon(String userRole, UUID couponId) {
        Coupon coupon = getCouponById(couponId);
        if (coupon.isDelete()) {
            validateIsAdmin(userRole);
        }

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(coupon.getTarget());
        return couponHandler.getCouponResponseDto(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public GetIssuedCouponResponseDto getIssuedCoupon(Long currentUserId, String currentUserRole, UUID couponId, Long userId) {
        if (isCustomer(currentUserRole) && !currentUserId.equals(userId)) {
            throw new UnauthorizedAccessException();
        }

        Coupon coupon = getCouponById(couponId);

        if (coupon.isDelete()) {
            validateIsAdmin(currentUserRole);
        }

        CouponUser couponUser = getCouponUser(currentUserId, coupon);

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(coupon.getTarget());
        return couponHandler.getIssuedCouponResponseDto(coupon, couponUser);
    }

    @Override
    @Transactional
    public void apply(Long currentUserId, String currentUserRole, UUID couponId) {
        validateIsCustomer(currentUserRole);

        Coupon coupon = getCouponById(couponId);

        CouponUser couponUser = getCouponUser(currentUserId, coupon);

        couponUser.apply();
        couponUserRepository.save(couponUser);
    }

    private CouponUser getCouponUser(Long currentUserId, Coupon coupon) {
        return couponUserRepository.findByUserIdAndCoupon(currentUserId, coupon).orElseThrow(UserNotIssuedCouponException::new);
    }

    public Page<GetCouponResponseDto> getCoupons(String currentUserRole, String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable) {
        return couponRepository.getCoupons(currentUserRole, search, isDeleted, couponTarget, status, pageable);
    }
}
