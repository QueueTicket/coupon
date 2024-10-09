package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class FakeRepository implements CouponRepository {

    private Map<UUID, Coupon> couponStorage = new HashMap<>();

    @Override
    public Coupon save(Coupon coupon) {
        if (coupon.getId() == null) {
            UUID uuid = UUID.randomUUID();
            ReflectionTestUtils.setField(coupon, "id", uuid);
        }
        couponStorage.put(coupon.getId(), coupon);
        return coupon;
    }


    @Override
    public Optional<Coupon> findById(UUID couponId) {
        return Optional.of(couponStorage.get(couponId));
    }

}
