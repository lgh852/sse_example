package com.ghl.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void deleteById(String id);

    void saveEventCache(String emitterId, Object event);

    void deleteEventCacheStartWithTime(long currentTimeMillis);

    Map<String, SseEmitter> findAllEmitterStartWithByUserIdIn(List<Long> userIdList);

    Map<String, Object> findEventCacheStartWithTime(long currentTimeMillis);


    /*
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);

    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);

    void deleteAllEmitterStartWithId(String memberId);

    void deleteAllEventCacheStartWithId(String memberId);

    Map<String, SseEmitter> findAllEmitter();*/
}
