package com.qticket.coupon.config;

import com.qticket.common.config.AuditorAwareImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class KafkaListenerAspect {

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object setUserIdAroundKafkaListener(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method method = getMethodFromJoinPoint(joinPoint);
        if (method == null) {
            return joinPoint.proceed();
        }

        try {
            // 메서드 파라미터의 어노테이션을 검사하여 @Header로 마킹된 값을 찾음
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Header headerAnnotation = parameters[i].getAnnotation(Header.class);
                if (headerAnnotation != null && "X-USER-ID".equals(headerAnnotation.value())) {
                    // @Header("X-USER-ID")에 해당하는 인자 값 추출
                    String userId = (String) args[i];
                    if (userId != null) {
                        // 사용자 ID를 ThreadLocal에 설정
                        AuditorAwareImpl.setAsyncUserId(userId);
                    }
                }
            }

            // 실제 KafkaListener 메서드 실행
            return joinPoint.proceed();
        } finally {
            // 작업이 완료된 후 ThreadLocal 초기화
            AuditorAwareImpl.clearAsyncUserId();
        }
    }

    // JoinPoint로부터 Method를 얻는 헬퍼 메서드
    private Method getMethodFromJoinPoint(ProceedingJoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Class<?> targetClass = joinPoint.getTarget().getClass();
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) &&
                        method.getParameterCount() == joinPoint.getArgs().length) {
                    return method;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}