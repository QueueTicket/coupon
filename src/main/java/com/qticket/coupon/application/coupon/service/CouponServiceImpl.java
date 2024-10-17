package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.application.cache.CacheRepository;
import com.qticket.coupon.application.coupon.dto.request.*;
import com.qticket.coupon.application.coupon.dto.response.*;
import com.qticket.coupon.application.coupon.exception.*;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeHandler;
import com.qticket.coupon.application.coupon.service.coupontargethandler.CouponTypeRegistry;
import com.qticket.coupon.application.message.Producer;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import com.qticket.coupon.domain.couponuser.model.CouponUser;
import com.qticket.coupon.domain.couponuser.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

        validateCreateRequestDto(requestDto);

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(requestDto.getTarget());
        Coupon coupon = couponHandler.create(requestDto);

        if (coupon.hasMaxQuantity()) {
            cacheRepository.save(coupon);
        }

        return new CouponCreateResponseDto(coupon.getId());
    }

    private void validateCreateRequestDto(CouponCreateRequestDto requestDto) {
        validateCouponTime(requestDto);
        validateDiscountAmount(requestDto);
        validateDiscountPercentage(requestDto);
    }

    private void validateCouponTime(CouponCreateRequestDto requestDto) {
        if (requestDto.getStartDate().isAfter(requestDto.getExpirationDate()) || requestDto.getStartDate().isEqual(requestDto.getExpirationDate())) {
            throw new CouponTimeInvalidException();
        }

        if (requestDto.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new CouponTimeInvalidException();
        }

        if (requestDto.getStartDate().isBefore(LocalDateTime.now())) {
            throw new CouponTimeInvalidException();
        }
    }

    private void validateDiscountAmount(CouponCreateRequestDto requestDto) {
        if (DiscountPolicy.FIXED.equals(requestDto.getDiscountPolicy()) && requestDto.getDiscountAmount() != requestDto.getMaxDiscountPrice()) {
            throw new DiscountAmountNotMatchMaxDiscountPriceException();
        }
    }

    private void validateDiscountPercentage(CouponCreateRequestDto requestDto) {
        if (DiscountPolicy.PERCENTAGE.equals(requestDto.getDiscountPolicy()) && requestDto.getDiscountAmount() > 100) {
            throw new DiscountRateExceedsLimitException();
        }
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
        Coupon coupon = getCouponById(issueByAdminRequestDto.getCouponId());

        validateCouponStartDate(coupon.getStartDate());
        validateUnLimitedCoupon(coupon);

        producer.sendIssueByAdminMessage(userId, userRole, issueByAdminRequestDto);
    }

    private void validateUnLimitedCoupon(Coupon coupon) {
        if (coupon.getMaxQuantity() != -1) {
            throw new AdminIssueOnlyUnlimitedCouponException();
        }
    }

    private boolean isStartDateBeforeNow(LocalDateTime startDate) {
        return LocalDateTime.now().isBefore(startDate);
    }

    private void validateCouponStartDate(LocalDateTime startDate) {
        if (isStartDateBeforeNow(startDate)) {
            throw new CouponTimeInvalidException();
        }
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
            validateCustomerIssue(stockQuantity, couponId, coupon.getStartDate());
            cacheRepository.issueCoupon(couponId, userId);
        }

        producer.sendIssueByCustomerMessage(userId, userRole, issueByCustomerRequestDto);
    }

    private boolean isInvalidCouponQuantity(Long couponQuantity) {
        return couponQuantity != null && couponQuantity < 0;
    }

    private void validateCustomerIssue(Long couponQuantity, UUID couponId, LocalDateTime startDate) {
        if (isInvalidCouponQuantity(couponQuantity) || isStartDateBeforeNow(startDate)) {
            cacheRepository.increaseCouponQuantity(couponId);
            throw new CouponOutOfStockException();
        }

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




    private void validateIsCustomer(String userRole) {
        if (!isCustomer(userRole)) {
            throw new UnauthorizedAccessException();
        }
    }
    private boolean isCustomer(String userRole) {
        return "CUSTOMER".equals(userRole);
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

    @Override
    public Page<GetCouponResponseDto> getCoupons(String currentUserRole, String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable) {

        if (isDeleted != null && List.of("ALL", "DELETED").contains(isDeleted) && !isAdmin(currentUserRole)) {
            throw new UnauthorizedAccessException();
        }

        return couponRepository.getCoupons(search, isDeleted, couponTarget, status, pageable);
    }

    @Override
    public Page<GetIssuedCouponResponseDto> getIssuedCoupons(Long currentUserId, String currentUserRole, Long userId, String isDeleted, CouponTarget couponTarget, String usable, UUID eventId, Pageable pageable) {
        if (!isAdmin(currentUserRole) && currentUserId != userId) {
            throw new UnauthorizedAccessException();
        }
        return couponRepository.getIssuedCoupons(userId, isDeleted, couponTarget, usable, eventId, pageable);
    }

    @Override
    public Page<GetCouponByAdminResponseDto> getCouponsByAdmin(Long currentUserId, String currentUserRole, Long userId, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable) {
        if (!isAdmin(currentUserRole)) {
            throw new UnauthorizedAccessException();
        }
        return couponRepository.getCouponsByAdmin(userId, isDeleted, couponTarget, status, pageable);
    }


    // 1. 최소 금액 검증
    // 2. 유저가 발급 받은 쿠폰인지 검증
    // 3. 만료 시간 검증
    // 4. 사용 횟수 검증
    // 5. 각 쿠폰 별로 로직 검증
    @Override
    public CouponValidateResponseDto validate(Long userId, UUID couponId, UUID eventId, Long price) {
        Coupon coupon = getCouponById(couponId);
        CouponUser couponUser = getCouponUser(userId, coupon);

        if (coupon.getMinSpendAmount() > price) {
            throw new MinimumPriceNotSatisfiedException();
        }

        // 만료 검증
        // 삭제된 쿠폰인지 검증
        coupon.validateUsable();

        // 사용 가능 횟수 검증
        couponUser.validateUsable();

        CouponTypeHandler couponHandler = couponTypeRegistry.getCouponHandler(coupon.getTarget());
        couponHandler.validate(userId, coupon, eventId);

        return new CouponValidateResponseDto(couponId, coupon.getDiscountAmount(), coupon.getDiscountPolicy(), coupon.getDiscountAmount());
    }

}
