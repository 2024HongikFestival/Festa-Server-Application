package com.hyyh.festa.controller;

import com.hyyh.festa.domain.Event;
import com.hyyh.festa.dto.EventPostRequest;
import com.hyyh.festa.dto.EventResponse;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/admin/events")
    public ResponseEntity<ResponseDTO<?>> createEvent(@RequestBody EventPostRequest eventPostRequest) {
        EventResponse createdEvent = eventService.createEvent(eventPostRequest);
        ResponseDTO<?> responseDTO =ResponseDTO.ok("이벤트 생성 성공", createdEvent);
        return ResponseEntity.status(200).body(responseDTO);
    };

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<ResponseDTO<?>> updateEvent(@PathVariable Long eventId, @RequestBody EventPostRequest eventPostRequest) {
        try{
            EventResponse updatedevent = eventService.updateEvent(eventId, eventPostRequest);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("이벤트 수정 성공", updatedevent);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    };

    @GetMapping("/events/{eventId}")
    public ResponseEntity<ResponseDTO<?>> getOneEvent(@PathVariable Long eventId) {
        try{
            EventResponse event = eventService.getOneEvent(eventId);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("이벤트 단건 조회 성공", event);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    };

    @GetMapping("/events")
    public ResponseEntity<ResponseDTO<?>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        ResponseDTO<?> responseDTO = ResponseDTO.ok("이벤트 리스트 조회 성공", events);
        return ResponseEntity.status(200).body(responseDTO);
    }

    @DeleteMapping("/admin/events/{eventId}")
    public ResponseEntity<ResponseDTO<?>> deleteEvent(@PathVariable Long eventId) {
        try{
            eventService.deleteEvent(eventId);
            ResponseDTO<?> responseDTO = ResponseDTO.custom(HttpStatus.NO_CONTENT,
                    "이벤트 삭제 성공", Collections.emptyMap());
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    };
}
