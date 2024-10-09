package com.qticket.coupon.domain.couponuser.model;

import com.qticket.coupon.domain.coupon.model.Coupon;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "coupon_users")
public class CouponUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    private Coupon coupon;

    public static CouponUser create(Long userId, Coupon coupon) {
        CouponUser couponUser = new CouponUser();
        couponUser.userId = userId;
        couponUser.coupon = coupon;
        return couponUser;
    }
}
