package com.ghl.sse.repository.impl;

import com.ghl.sse.repository.EmitterRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    //
    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public void deleteEventCacheStartWithTime(long currentTimeMillis) {

        emitters.forEach(
                (key, emitter) -> {
                    if (Long.valueOf(key.toString().split("_")[1]) < currentTimeMillis) {
                        emitters.remove(key);
                    }
                }
        );

    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByUserIdIn(List<Long> userIdList) {

        return emitters.entrySet().stream()
                .filter(entry -> userIdList.stream()
                        .anyMatch(userId -> userId.equals(entry.getKey().split("_")[0]))
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findEventCacheStartWithTime(long currentTimeMillis) {



        //
        eventCache.entrySet().stream()
                .filter(entry ->
                        Long.valueOf(entry.toString().split("_")[1]) < currentTimeMillis
//                        entry -> entry.getKey().startsWith(memberId)
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        eventCache.forEach(
                (key, emitter) -> {
                    if (Long.valueOf(key.toString().split("_")[1]) < currentTimeMillis) {
                        emitters.remove(key);
                    }
                }
        );

        return null;
    }


//
//    @Override
//    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
//        return emitters.entrySet().stream()
//                .filter(entry -> entry.getKey().startsWith(memberId))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    @Override
//    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
//        return eventCache.entrySet().stream()
//                .filter(entry -> entry.getKey().startsWith(memberId))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//

//
//    @Override
//    public void deleteAllEmitterStartWithId(String memberId) {
//        emitters.forEach(
//                (key, emitter) -> {
//                    if (key.startsWith(memberId)) {
//                        emitters.remove(key);
//                    }
//                }
//        );
//    }
//
//    @Override
//    public void deleteAllEventCacheStartWithId(String memberId) {
//        eventCache.forEach(
//                (key, emitter) -> {
//                    if (key.startsWith(memberId)) {
//                        eventCache.remove(key);
//                    }
//                }
//        );
//    }
//
//    @Override
//    public Map<String, SseEmitter> findAllEmitter() {
//        return emitters;
//    }

}
