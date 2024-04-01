package com.example.laboratory.controller;

import com.example.laboratory.entity.Member;
import com.example.laboratory.util.NotificationType;
import com.example.laboratory.repository.MemberRepository;
import com.example.laboratory.response.NotificationResponse;
import com.example.laboratory.service.NotificationService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lab")
public class LabController {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @GetMapping(value = "/subscribe")
    public DeferredResult<List<NotificationResponse>> getNewNotifications(Principal principal) {
        Member member = memberRepository.findByUsername(principal.getName())
                .orElseThrow(RuntimeException::new);

        // DeferredResult 생성 및 저장소에 저장
        DeferredResult<List<NotificationResponse>> deferredResult =
                notificationService.getDeferredResult("0");

        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(new RuntimeException());
        });

        return deferredResult;
    }

    @PostMapping(value = "/send")
    public void sendNotification() {
        notificationService.sendArtistNotification(NotificationType.NEW_ARTICLE);
    }

}
