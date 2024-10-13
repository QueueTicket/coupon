package com.qticket.coupon.infrastructure.coupon.repository;

import com.qticket.coupon.application.coupon.dto.response.GetCouponResponseDto;
import com.qticket.coupon.application.coupon.exception.UnauthorizedAccessException;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.model.Coupon;
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

import static com.qticket.coupon.domain.coupon.model.QCoupon.coupon;

@Repository
@RequiredArgsConstructor
public class CouponQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<GetCouponResponseDto> getCoupons(String currentUserRole, String search, String isDeleted, CouponTarget couponTarget, String status, Pageable pageable) {
        List<Coupon> couponList = jpaQueryFactory
                .selectFrom(coupon)
                .where(
                        search(search),
                        targetEq(couponTarget),
                        statusEq(status),
                        isDeleted(isDeleted, currentUserRole)
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
                        isDeleted(isDeleted, currentUserRole)
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

    private BooleanExpression isDeleted(String isDeleted, String userRole) {
        if ("ALL".equals(isDeleted)) {
            if (!"ADMIN".equals(userRole)) {
                throw new UnauthorizedAccessException();
            }
            return Expressions.TRUE;
        }

        if ("Deleted".equals(isDeleted)) {
            if (!"ADMIN".equals(userRole)) {
                throw new UnauthorizedAccessException();
            }
            return coupon.isDelete.isTrue();
        }

        return coupon.isDelete.isFalse();
    }

    private BooleanExpression statusEq(String status) {

        if ("ALL".equals(status)) {
            return Expressions.TRUE;
        }

        if ("Expired".equals(status)) {
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
}
