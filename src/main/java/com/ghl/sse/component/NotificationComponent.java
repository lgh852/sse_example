package com.ghl.sse.component;

import com.ghl.sse.repository.EmitterRepository;
import com.ghl.sse.type.NotificationType;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.ghl.sse.component
 * fileName       : NotificationComponent
 * author         : lgh
 * date           : 2022/11/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/29        lgh       최초 생성
 */
@Configuration
@RequiredArgsConstructor
public class NotificationComponent {

    private final EmitterRepository emitterRepository;

    public void SendToNotification(NotificationType notificationType) {

        // case 1. scheduler 일경우
        // case 2. 입고,주문 등이 생성되어 알림이 필요할경우

        // TODO : 해당 Noti Type 의 메세지를 받을수 있는 Member의 리스트 조회 필요
        List<Long> userIdList = new ArrayList<>();


        // TODO : DB 접근 Content Template 조회
        HashMap<String, String> contentMap = new HashMap<>();

        // member Id 기준 현재 Connection 이 맺어진 ID 목록 접근 필요
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserIdIn(userIdList);

        this.sendToNotification(emitters, contentMap);
        // Send 처리

        // DB 접근 Noti 내역 생성

    }


    public void sendToNotification(Map<String, SseEmitter> emitters, HashMap<String, String> contentMap) {

        // Cache 내역 추가
        String eventId = this.makeEventId(NotificationType.valueOf(contentMap.get("notificationType")));
        emitterRepository.saveEventCache(
                eventId,
                contentMap
        );

        // 10분이 지난 Cache 제거
        emitterRepository.deleteEventCacheStartWithTime(System.currentTimeMillis() - (1000 * 60 * 10));

        // Send 처리
        emitters.forEach(
                (key, emitter) -> {
                    sendNotification(emitter, eventId, key, contentMap);
                }
        );

    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {

            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));

        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }

    }

    public String makeEmitterId() {
        // TODO : Security Context에 접근하여 userId 조회
        Long userID = 1L;

        return makeTimeIncludeId(userID.toString());
    }

    public String makeEventId(NotificationType notificationType) {

        return makeTimeIncludeId(notificationType.getValue());
    }

    private String makeTimeIncludeId(String key) {

        // 시간 비교를 위해 currentTimeMillis 사용
        return key + "_" + System.currentTimeMillis()+ RandomStringUtils.randomNumeric(6);
    }

}
