package com.qticket.coupon.domain.couponseatgrade.model;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.qticket.common.BaseEntity;
import com.qticket.coupon.domain.coupon.model.Coupon;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "coupon_seat_grade")
public class CouponSeatGrade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID eventId;
    @Column
    private String seatGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

}
