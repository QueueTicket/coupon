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
        System.out.println("쿠폰 저장됨: couponId = " + coupon.getId());
        System.out.println("couponStorage = " + couponStorage);
        Coupon coupon1 = couponStorage.get(coupon.getId());
        System.out.println("coupon1 = " + coupon1.getId());
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(UUID couponId) {
        System.out.println("쿠폰 조회 요청: couponId = " + couponId);
        System.out.println("couponStorage = " + couponStorage);
        return Optional.ofNullable(couponStorage.get(couponId));
    }

}
