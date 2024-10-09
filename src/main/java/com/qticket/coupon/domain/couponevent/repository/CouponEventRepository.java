package com.qticket.coupon.domain.couponevent.repository;

import com.qticket.coupon.domain.couponevent.model.CouponEvent;

import java.util.List;
import java.util.Objects;

public interface CouponEventRepository {

    void saveAll(List<CouponEvent> couponEvents);

}
