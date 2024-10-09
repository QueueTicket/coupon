package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.domain.couponevent.model.CouponEvent;
import com.qticket.coupon.domain.couponevent.repository.CouponEventRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakeCouponEventRepository implements CouponEventRepository {

    private Map<UUID, CouponEvent> couponEventStorage = new HashMap<>();

    private FakeCouponEventRepository() {

    }

    private static class LazyHolder {
        private static FakeCouponEventRepository instance = new FakeCouponEventRepository();
    }

    public static FakeCouponEventRepository getInstance() {
        return LazyHolder.instance;
    }

    @Override
    public void saveAll(List<CouponEvent> couponEvents) {
        couponEvents.forEach(couponEvent ->
                couponEventStorage.put(couponEvent.getId(), couponEvent));

    }
}
