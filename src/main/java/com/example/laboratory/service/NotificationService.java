package com.example.laboratory.service;

import com.example.laboratory.entity.Member;
import com.example.laboratory.repository.MemberRepository;
import com.example.laboratory.util.Channel;
import com.example.laboratory.entity.Notification;
import com.example.laboratory.util.NotificationType;
import com.example.laboratory.repository.DeferredResultRepository;
import com.example.laboratory.repository.NotificationRepository;
import com.example.laboratory.response.NotificationResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private final NotificationRepository notificationRepository;
    private final DeferredResultRepository deferredResultRepository;

    private final static Long TIMEOUT = 60000L;
    private final MemberRepository memberRepository;

    @Transactional
    public void sendArtistNotification(NotificationType notificationType) {

        Member subscriber = memberRepository.findById(1L)
                .orElseThrow(RuntimeException::new);

        Member publisher = memberRepository.findById(2L)
                .orElseThrow(RuntimeException::new);

        // 팔로워 별로 알림 생성
        Notification notification = notificationRepository.save(Notification.builder()
                .message(notificationType.getMessage())
                .subscriber(subscriber)
                .publisher(publisher)
                .redirectUrl("redirectUrl")
                .build()
        );

        // Redis Publish, 팔로워에게 메시지 전송 ex_) 2:1:아티스트님의 새 앨범이 등록되었습니다.
        redisTemplate.convertAndSend(Channel.ARTIST_NOTIFICATION.getName(), "0"
                + ":" + notification.getId()
                + ":" + notificationType.getMessage()
        );
    }

    public DeferredResult<List<NotificationResponse>> getDeferredResult(String key) {
        return deferredResultRepository.findByKey(key)
                .orElseGet(() -> {
                    DeferredResult<List<NotificationResponse>> deferredResult = new DeferredResult<>(TIMEOUT);
                    deferredResultRepository.save(key, deferredResult);
                    return deferredResult;
                });
    }

}
