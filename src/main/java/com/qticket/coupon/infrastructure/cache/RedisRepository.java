package com.qticket.coupon.infrastructure.cache;

import com.qticket.coupon.application.cache.CacheRepository;
import com.qticket.coupon.domain.coupon.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RedisRepository implements CacheRepository {

    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final String COUPON_PREFIX = "coupon:";
    private final String CUSTOMER_PREFIX = "customer:";

    @Override
    public void save(Coupon coupon) {
        Duration duration = calculateTTL(coupon.getExpirationDate());
        integerRedisTemplate.opsForValue()
                .set(COUPON_PREFIX + coupon.getId(), coupon.getMaxQuantity(), duration);
    }

    private Duration calculateTTL(LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, expirationDate);
    }

    @Override
    public Integer getCouponQuantityById(UUID couponId) {
        return integerRedisTemplate.opsForValue()
                .get(COUPON_PREFIX + couponId);
    }

    @Override
    public boolean isExistByCouponIdAndUserId(UUID couponId, Long userId) {
        String key = CUSTOMER_PREFIX + userId;
        String expectedValue = COUPON_PREFIX + couponId;

        String actualValue = redisTemplate.opsForValue().get(key);
        return expectedValue.equals(actualValue);
    }

    @Override
    public Long decreaseCouponQuantity(UUID couponId) {
        return integerRedisTemplate.opsForValue()
                .decrement(COUPON_PREFIX + couponId);
    }

    @Override
    public void issueCoupon(UUID couponId, Long userId) {
        Duration duration = Duration.ofSeconds(integerRedisTemplate.getExpire(COUPON_PREFIX + couponId));
        redisTemplate.opsForValue()
                .set(CUSTOMER_PREFIX + userId, COUPON_PREFIX + couponId, duration);
    }

    @Override
    public void increaseCouponQuantity(UUID couponId) {
        integerRedisTemplate.opsForValue()
                .increment(COUPON_PREFIX + couponId);
    }


}
