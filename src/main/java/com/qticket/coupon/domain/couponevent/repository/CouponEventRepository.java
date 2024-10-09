package com.qticket.coupon.domain.couponevent.repository;

import com.qticket.coupon.domain.couponevent.model.CouponEvent;

import java.util.List;

public interface CouponEventRepository {

    void saveALl(List<CouponEvent> couponEvents);
}
