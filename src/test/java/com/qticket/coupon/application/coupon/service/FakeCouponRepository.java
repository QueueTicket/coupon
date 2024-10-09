package com.qticket.coupon.application.coupon.service;

import com.qticket.coupon.domain.coupon.model.Coupon;
import com.qticket.coupon.domain.coupon.repository.CouponRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class FakeCouponRepository implements CouponRepository {

    private final Map<UUID, Coupon> couponStorage = new HashMap<>();

    private static class LazyHolder {
        private static FakeCouponRepository instance = new FakeCouponRepository();
    }
    private FakeCouponRepository() {

    }

    public static FakeCouponRepository getInstance() {
        return LazyHolder.instance;
    }
    @Override

    public Coupon save(Coupon coupon) {
        if (coupon.getId() == null) {
            UUID uuid = UUID.randomUUID();
            ReflectionTestUtils.setField(coupon, "id", uuid);
        }
        couponStorage.put(coupon.getId(), coupon);
        Coupon coupon1 = couponStorage.get(coupon.getId());
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(UUID couponId) {

        return Optional.ofNullable(couponStorage.get(couponId));
    }

}
