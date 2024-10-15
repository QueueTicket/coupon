package com.qticket.coupon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qticket.common.dto.ResponseDto;
import com.qticket.common.exception.ClientException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
public class FeignConfig {

    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜 및 시간 모듈 등록
        return objectMapper;
    }

    @Bean
    public Decoder customDecoder() {
        return new CustomDecoder(new JacksonDecoder(customObjectMapper()));
    }

    @Bean
    public ErrorDecoder customErrorDecoder() {
        return new CustomErrorDecoder();
    }

    class CustomErrorDecoder implements ErrorDecoder {
        private final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);

        @Override
        public Exception decode(String methodKey, Response response){
            byte[] bodyData = new byte[0];
            ResponseDto<?> responseDto;
            try {
                bodyData = response.body().asInputStream().readAllBytes();
                String body = new String(bodyData);
                responseDto = customObjectMapper().readValue(body, ResponseDto.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            throw new ClientException(HttpStatus.valueOf(response.status()), responseDto.getMessage());
        }
    }

     class CustomDecoder implements Decoder {
        private final Logger logger = LoggerFactory.getLogger(CustomDecoder.class);
        private final Decoder defaultDecoder;

        public CustomDecoder(Decoder defaultDecoder) {
            this.defaultDecoder = defaultDecoder;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException {
            // InputStream을 먼저 캐싱
            byte[] bodyData = response.body().asInputStream().readAllBytes();

            logger.info("Response status: {}", response.status());
            logger.info("Response body: {}", new String(bodyData));

            // 캐싱된 데이터를 이용해 응답 처리
            Response modifiedResponse = response.toBuilder()
                    .body(bodyData)
                    .build();

            return defaultDecoder.decode(modifiedResponse, type);
        }
    }
}