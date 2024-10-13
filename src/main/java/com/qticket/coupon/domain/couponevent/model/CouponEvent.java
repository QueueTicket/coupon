package com.qticket.coupon.domain.couponevent.model;

import com.qticket.common.BaseEntity;
import com.qticket.coupon.domain.coupon.model.Coupon;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity(name = "coupon_events")
@Getter
public class CouponEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID eventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public static CouponEvent create(UUID eventId, Coupon coupon) {
        CouponEvent couponEvent = new CouponEvent();
        couponEvent.eventId = eventId;
        couponEvent.coupon = coupon;
        return couponEvent;
    }
}
