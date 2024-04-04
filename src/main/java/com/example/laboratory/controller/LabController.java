package com.example.laboratory.controller;

import ch.qos.logback.core.model.Model;
import com.example.laboratory.entity.Member;
import com.example.laboratory.util.NotificationType;
import com.example.laboratory.repository.MemberRepository;
import com.example.laboratory.response.NotificationResponse;
import com.example.laboratory.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
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

    @GetMapping("/async-deferredresult")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(Model model, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        // Create a new SecurityContext and set the authentication
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        // Save the SecurityContext to the request
        RequestAttributeSecurityContextRepository requestAttributeSecurityContextRepository = new RequestAttributeSecurityContextRepository();
        requestAttributeSecurityContextRepository.saveContext(securityContext, request, response);

        ForkJoinPool.commonPool().submit(() -> {
            log.info("Processing in separate thread");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
            output.setResult(ResponseEntity.ok("ok"));
        });

        log.info("servlet thread freed");
        return output;
    }


    @PostMapping(value = "/send")
    public void sendNotification() {
        notificationService.sendArtistNotification(NotificationType.NEW_ARTICLE);
    }

}
