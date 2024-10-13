package com.qticket.coupon.domain.coupon.model;

import com.qticket.common.BaseEntity;
import com.qticket.coupon.application.coupon.exception.CouponAlreadyDeletedException;
import com.qticket.coupon.application.coupon.exception.CouponAlreadyExpiredException;
import com.qticket.coupon.domain.coupon.enums.CouponTarget;
import com.qticket.coupon.domain.coupon.enums.DiscountPolicy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "coupons")
@Getter
@NoArgsConstructor
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountPolicy discountPolicy;

    @Column(nullable = false)
    private int maxDiscountPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponTarget target;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private int minSpendAmount;

    @Column(nullable = false)
    private int maxQuantity;

    @Column(nullable = false)
    private int issuedQuantity = 0;

    @Column(nullable = false)
    private int usageLimit;

    @Column(nullable = false)
    private int usageQuantity = 0;

    public static Coupon create(String name,
                                int discountAmount,
                                DiscountPolicy discountPolicy,
                                int maxDiscountPrice,
                                CouponTarget target,
                                LocalDateTime startDate,
                                LocalDateTime expirationDate,
                                int minSpendAmount,
                                int maxQuantity,
                                int usageLimit
    ) {
        Coupon coupon = new Coupon();
        coupon.name = name;
        coupon.discountAmount = discountAmount;
        coupon.discountPolicy = discountPolicy;
        coupon.target = target;
        coupon.startDate = startDate;
        coupon.expirationDate = expirationDate;
        coupon.minSpendAmount = minSpendAmount;
        coupon.maxQuantity = maxQuantity;
        coupon.usageLimit = usageLimit;
        return coupon;
    }

    public void update(String name,
                         int discountAmount,
                         DiscountPolicy discountPolicy,
                         CouponTarget target,
                         LocalDateTime startDate,
                         LocalDateTime expirationDate,
                         int minSpendAmount,
                         int maxQuantity,
                         int usageLimit
    ) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.discountPolicy = discountPolicy;
        this.target = target;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.minSpendAmount = minSpendAmount;
        this.maxQuantity = maxQuantity;
        this.usageLimit = usageLimit;
    }

    public void issue() {
        this.issuedQuantity ++;
    }
    public boolean hasMaxQuantity() {
        return maxQuantity != -1;
    }

    public void apply() {
        validateUsable();
        usageQuantity += 1;
    }

    private void validateUsable() {
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new CouponAlreadyExpiredException();
        }

        if (isDelete()) {
            throw new CouponAlreadyDeletedException();
        }
    }
}
