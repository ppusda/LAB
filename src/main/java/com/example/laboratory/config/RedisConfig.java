package com.example.laboratory.config;

import com.example.laboratory.util.ArtistNotificationListener;
import com.example.laboratory.util.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    private final ArtistNotificationListener artistNotificationListener;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        // redisTemplate를 받아와서 set, get, delete를 사용
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        // setKeySerializer, setValueSerializer 설정
        // redis-cli을 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(notificationListenerAdapter(), topicNotification());
        return container;
    }

    @Bean
    MessageListenerAdapter notificationListenerAdapter() {
        return new MessageListenerAdapter(artistNotificationListener);
    }

    @Bean
    public ChannelTopic topicStatus() {
        return new ChannelTopic(Channel.ENCODING_STATUS.getName());
    }

    @Bean
    public ChannelTopic topicNotification() {
        return new ChannelTopic(Channel.ARTIST_NOTIFICATION.getName());
    }

}
