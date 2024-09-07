package com.ite.sws.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 비동기 처리 관련 Configuration
 * @author 김민정
 * @since 2024.09.07
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.07  	김민정       최초 생성
 * </pre>
 */
@EnableAsync
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 비동기 작업을 위한 스레드 풀 설정 메서드
     * @return Executor 비동기 작업을 처리할 스레드 풀을 설정 반환
     */
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 수
        executor.setMaxPoolSize(50); // 최대 스레드 수
        executor.setQueueCapacity(100); // 대기열 크기
        executor.setThreadNamePrefix("Executor-");

        // 대기열이 꽉 찼을 때 처리할 정책 설정
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 호출한 쓰레드에서 작업을 직접 실행하여 처리

        executor.initialize();
        return executor;
    }

    /**
     * 비동기 예외 처리 핸들러
     * @return AsyncUncaughtExceptionHandler 인터페이스를 구현하여 비동기 메서드에서 발생하는 예외를 처리
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("비동기 처리 중 예외가 발생했습니다. 메서드: {}, 예외 메시지: {}", method.getName(), throwable.getMessage());
            throwable.printStackTrace(); // 예외 로깅
        };
    }
}
