package com.ghl.sse.service;

import com.ghl.sse.component.NotificationComponent;
import com.ghl.sse.repository.EmitterRepository;
import com.ghl.sse.type.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {


    private final NotificationComponent notificationComponent;

    private Long DEFAULT_TIMEOUT = 60L * 1000L;

    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {

        AtomicInteger logCount = new AtomicInteger();

        String emitterId = notificationComponent.makeEmitterId();
        log.info("{} Emitter makeTimeIncludeId emitterId : {}, {}", logCount.getAndIncrement(), emitterId, lastEventId);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // onCompletion 비동기 처리 도중 에러 등의 이유로 더이상의 응답을 처리하지 못할경우
        // Ex : client의 변경으로 더이상 응답을 보낼수 없을경우
        emitter.onCompletion(() -> {
                    log.info("{} Emitter onCompletion emitterId : {}", logCount.getAndIncrement(), emitterId);
                    emitterRepository.deleteById(emitterId);
                }
        );

        // SseEmitter 생성시 생성된 시간이 만료 되었을경우
        emitter.onTimeout(() -> {
                    log.info("{} Emitter onTimeout emitterId : {}", logCount.getAndIncrement(), emitterId);
                    emitterRepository.deleteById(emitterId);
                }
        );

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        // 연결된후 한번의 메세지도 전송되지 않을경우 503
        String eventId = notificationComponent.makeEventId(NotificationType.SUBSCRIBE);

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {

            log.info("{} Emitter hasLostData lastEventId : {}", logCount.getAndIncrement(), emitterId);
            // TODO : Client 재연결 시도시 3초 정도의 시간 소요됨 그사이 추가 발생된 메세지 존재 유무 확인
//            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }





    private void sendLostData(String lastEventId, String emitterId, SseEmitter emitter) {

        // 디비에 접근한다 .

        // UserId : 기준 디비에 접근하여
        // Retry 시간 동안 발생한 Noti의 내용을 처리하기 위함

        // eventID + DEFAULT_TIMEOUT 은

        emitterRepository.findEventCacheStartWithTime(lastEventId.split("-")[0] + DEFAULT_TIMEOUT);




//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

//
//    public void send(String content, String url) {
////        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));
//
////        String receiverId = String.valueOf(receiver.getId());
//
//        // TODO : 알림 타입으로 대체
//        String receiverId = "SYSTEM";
//
//        String eventId = receiverId + "_" + System.currentTimeMillis();
//
//        // 연결되어 있는 모든
////        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
//
//        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitter();
//
//        emitters.forEach(
//                (key, emitter) -> {
//                    emitterRepository.saveEventCache(key, content);
//                    sendNotification(emitter, eventId, key, content);
//                }
//        );
//    }


//    private Notification createNotification(Member receiver, NotificationType notificationType, String content, String url) {
//        return Notification.builder()
//                .receiver(receiver)
//                .notificationType(notificationType)
//                .content(content)
//                .url(url)
//                .isRead(false)
//                .build();
//    }

}
