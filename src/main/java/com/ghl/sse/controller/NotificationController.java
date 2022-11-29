package com.ghl.sse.controller;

import com.ghl.sse.emitters.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final SseEmitters sseEmitters;


    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(emitter);
    }

    @RequestMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmitteer() throws IOException {

        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);

        emitter.send(SseEmitter.event()
                .name("메세지입니다 + " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .data("connected!"));
    }

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView model) {

        model.setViewName("index");
        return model;
    }
}
