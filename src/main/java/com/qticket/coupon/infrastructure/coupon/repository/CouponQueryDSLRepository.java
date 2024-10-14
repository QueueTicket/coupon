package com.qticket.coupon.infrastructure.coupon.repository;

import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.dto.response.GetIssuedCouponResponseDto;
import com.qticket.coupon.application.coupon.exception.UnauthorizedAccessException;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.couponevent.model.QCouponEvent;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.qticket.coupon.domain.coupon.model.QCoupon.coupon;
import static com.qticket.coupon.domain.couponevent.model.QCouponEvent.couponEvent;
import static com.qticket.coupon.domain.couponuser.model.QCouponUser.couponUser;

@Repository
@RequiredArgsConstructor
public class CouponQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<GetCouponResponseDto> getCoupons(String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable) {
        List<Coupon> couponList = jpaQueryFactory
                .selectFrom(coupon)
                .where(
                        search(search),
                        targetEq(couponTarget),
                        statusEq(status),
                        isDeleted(isDeleted)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortOrder(pageable))
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(coupon.count())
                .where(
                        search(search),
                        targetEq(couponTarget),
                        statusEq(status),
                        isDeleted(isDeleted)
                );
        Page<Coupon> page = PageableExecutionUtils.getPage(couponList, pageable, countQuery::fetchOne);
        return page.map(GetCouponResponseDto::new);
    }

    private BooleanExpression targetEq(CouponTarget couponTarget) {
        return (couponTarget != null) ? coupon.target.eq(couponTarget) : Expressions.TRUE;
    }

    private BooleanExpression search(String search) {
        if (search != null && !search.isEmpty()) {
            return coupon.name.containsIgnoreCase(search);
        }

        return Expressions.TRUE;
    }

    private BooleanExpression isDeleted(String isDeleted) {
        if ("ALL".equals(isDeleted)) {
            return Expressions.TRUE;
        }

        if ("DELETED".equals(isDeleted)) {
            return coupon.isDelete.isTrue();
        }

        return coupon.isDelete.isFalse();
    }

    private BooleanExpression statusEq(String status) {

        if ("ALL".equals(status)) {
            return Expressions.TRUE;
        }

        if ("EXPIRED".equals(status)) {
            return coupon.expirationDate.before(LocalDateTime.now()).or(coupon.maxQuantity.ne(-1)
                    .and(coupon.maxQuantity.loe(coupon.issuedQuantity)));
        }

        return coupon.expirationDate.after(LocalDateTime.now())
                .and(coupon.maxQuantity.eq(-1)
                        .or(coupon.maxQuantity.gt(coupon.issuedQuantity)));

    }
    private OrderSpecifier<?> getSortOrder(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return null; // 정렬 조건이 없을 때
        }

        Sort.Order sortOrder = pageable.getSort().iterator().next(); // 첫 번째 정렬 기준 사용

        switch (sortOrder.getProperty()) {
            case "createdAt":
                return new OrderSpecifier<>(
                        sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        coupon.createdAt
                );
            case "updatedAt":
                return new OrderSpecifier<>(
                        sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        coupon.updatedAt
                );
            // 다른 필드에 대한 정렬 조건 추가
            default:
                throw new UnauthorizedAccessException();
        }
    }


    public Page<GetIssuedCouponResponseDto> getIssuedCoupons(Long userId, String isDeleted, CouponTarget couponTarget, String usable, UUID eventId, Pageable pageable) {
        List<Coupon> issuedCouponList = jpaQueryFactory
                .selectFrom(coupon)
                .leftJoin(couponUser).on(couponUser.coupon.eq(coupon))
                .leftJoin(couponEvent).on(couponEvent.coupon.eq(coupon))
                .where(
                        userEq(userId),
                        isDeleted(isDeleted),
                        eventTargetOrEventIdEq(couponTarget, eventId),
                        usableEq(usable)
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortOrder(pageable))
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(coupon.count())
                .from(coupon)
                .leftJoin(couponUser).on(couponUser.coupon.eq(coupon))
                .leftJoin(couponEvent).on(couponEvent.coupon.eq(coupon))
                .where(
                        userEq(userId),
                        isDeleted(isDeleted),
                        eventTargetOrEventIdEq(couponTarget, eventId),
                        usableEq(usable)
                );
        Page<Coupon> page = PageableExecutionUtils.getPage(issuedCouponList, pageable, countQuery::fetchOne);
        return page.map(GetIssuedCouponResponseDto::new);

    }

    private BooleanExpression userEq(Long userId) {
        return couponUser.userId.eq(userId);
    }

    private BooleanExpression usableEq(String usable) {
        if ("ALL".equals(usable)) {
            return Expressions.TRUE;
        }

        if ("UNUSABLE".equals(usable)) {
            return couponUser.usageLimit.loe(couponUser.usageCount)
                    .or(coupon.expirationDate.before(LocalDateTime.now()));

        }

        return couponUser.usageLimit.gt(couponUser.usageCount)
                .and(coupon.expirationDate.after(LocalDateTime.now()));

    }

    private BooleanExpression eventTargetOrEventIdEq(CouponTarget couponTarget, UUID eventId) {
        if (eventId != null) {
            // eventId가 전달된 경우: 해당 eventId에 적용 가능한 모든 쿠폰(ALL, EVENT)
            return coupon.target.eq(CouponTarget.ALL)
                    .or(coupon.target.eq(CouponTarget.EVENT).and(couponEvent.eventId.eq(eventId)));
        }

        // eventId가 없을 경우: 이벤트 타겟이 EVENT일 때만 해당 쿠폰을 보여줌
        return couponTarget != null && couponTarget.equals(CouponTarget.EVENT) ?
                coupon.target.eq(CouponTarget.EVENT) :
                coupon.target.eq(CouponTarget.ALL);
    }
}
