package com.qticket.coupon.domain.couponuser.model;

import com.qticket.common.BaseEntity;
import com.qticket.coupon.application.coupon.exception.UsageLimitExceedException;
import com.qticket.coupon.domain.coupon.model.Coupon;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "coupon_users")
@Getter
public class CouponUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name="coupon_id")
    private Coupon coupon;

    @Column
    private int usageCount = 0;
    @Column
    private int usageLimit;


    public static CouponUser create(Long userId, Coupon coupon) {
        CouponUser couponUser = new CouponUser();
        couponUser.userId = userId;
        couponUser.coupon = coupon;
        couponUser.usageLimit = coupon.getUsageLimit();
        return couponUser;
    }

    public void apply() {
        validateUsable();
        coupon.apply();
        usageCount += 1;
    }

    public void validateUsable() {
        if (usageCount >= usageLimit) {
            throw new UsageLimitExceedException();
        }
    }
}
