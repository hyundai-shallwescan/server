package com.ite.sws.config.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.domain.cart.event.CartRedisSubscriber;
import com.ite.sws.domain.cart.event.ChatRedisSubscriber;
import com.ite.sws.domain.cart.event.PaymentRedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 클래스
 * @author 정은지
 * @since 2024.08.30
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.30  	정은지        최초 생성
 * </pre>
 */

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);  // 비밀번호 설정

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        // 일반적인 key:value의 경우 시리얼라이저
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateObject() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter cartUpdateListener,
            MessageListenerAdapter chatListener,
            MessageListenerAdapter paymentListener,
            ChannelTopic cartUpdateTopic,
            ChannelTopic chatTopic,
            ChannelTopic paymentTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(cartUpdateListener, cartUpdateTopic);
        container.addMessageListener(chatListener, chatTopic);
        container.addMessageListener(paymentListener, paymentTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter cartUpdateListener(CartRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleCartUpdateMessage");
    }

    @Bean
    public MessageListenerAdapter chatListener(ChatRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleChatMessage");
    }

    @Bean
    public MessageListenerAdapter paymentListener(PaymentRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handlePaymentMessage");
    }

    @Bean
    public ChannelTopic cartUpdateTopic() {
        return new ChannelTopic("cartUpdateTopic");
    }

    @Bean
    public ChannelTopic chatTopic() {
        return new ChannelTopic("chatTopic");
    }

    @Bean
    public ChannelTopic paymentTopic() {
        return new ChannelTopic("paymentTopic");
    }

}
