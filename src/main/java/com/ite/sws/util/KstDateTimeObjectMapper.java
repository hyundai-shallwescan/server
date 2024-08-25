package com.ite.sws.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * KST 포맷터
 *
 * 전역적으로 JSON 직렬화 시 KST 시간대와 "yyyy-MM-dd HH:mm:ss" 형식으로 날짜를 처리하도록 설정
 *
 * @author 김민정
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	김민정      최초 생성
 * </pre>
 */
public class KstDateTimeObjectMapper extends ObjectMapper {

    public KstDateTimeObjectMapper() {
        // LocalDateTime을 처리하는 커스텀 직렬화기 등록
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new KstLocalDateTimeSerializer());
        this.registerModule(module);

        // 타임스탬프 대신 포맷된 날짜로 출력
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // LocalDateTime 타입 직렬화
    public static class KstLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        /**
         * LocalDateTime 값을 KST 시간대의 "yyyy-MM-dd HH:mm:ss" 형식으로 직렬화
         * @param value LocalDateTime
         * @param gen JsonGenerator
         * @param serializers SerializerProvider
         * @throws IOException
         */
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // UTC로 저장된 LocalDateTime을 KST로 변환
            ZonedDateTime kstDateTime = value.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
            String formattedDate = kstDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            gen.writeString(formattedDate);
        }
    }
}
