package com.ghl.sse.controller;

import com.ghl.sse.emitters.SseEmitters;
import com.ghl.sse.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable Long id,
                              @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(id, lastEventId);
    }

//    @GetMapping("/send")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void send(@RequestParam String content, String url) throws IOException {
//
//        notificationService.send(content, url);
//    }

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView model) {

        model.setViewName("index");
        return model;
    }
}
