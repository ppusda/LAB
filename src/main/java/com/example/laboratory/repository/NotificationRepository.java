package com.example.laboratory.repository;

import com.example.laboratory.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllBySubscriberIdOrderByCreateDateDesc(Long userId);
    void deleteAllByCreateDateBefore(LocalDateTime limitDate);
}
