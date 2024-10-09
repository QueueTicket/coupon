package com.qticket.coupon.domain.couponseat.model;

import com.qticket.coupon.domain.coupon.model.Coupon;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "coupon_seats")
public class CouponSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID seatId;

    @Column
    private UUID eventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
