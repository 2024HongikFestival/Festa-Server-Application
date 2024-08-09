package com.hyyh.festa.service;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.domain.Event;
import com.hyyh.festa.dto.EventPostRequest;
import com.hyyh.festa.dto.EventResponse;
import com.hyyh.festa.repository.EntryRepository;
import com.hyyh.festa.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;

    public EventResponse createEvent(EventPostRequest eventPostRequest) {
        Event newEvent = Event.builder()
                .title(eventPostRequest.getTitle())
                .prize(eventPostRequest.getPrize())
                .requires(eventPostRequest.getRequires())
                .startAt(eventPostRequest.getStartAt())
                .endAt(eventPostRequest.getEndAt())
                .announcedAt(eventPostRequest.getAnnouncedAt())
                .imageUrl(eventPostRequest.getImageUrl())
                .build();

        Event savedEvent = eventRepository.save(newEvent);
        return toEventResponse(savedEvent);
    };

    public EventResponse updateEvent(Long eventId, EventPostRequest eventPostRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트id"));
        String title = eventPostRequest.getTitle();
        String prize = eventPostRequest.getPrize();
        String requires = eventPostRequest.getRequires();
        LocalDateTime startAt = eventPostRequest.getStartAt();
        LocalDateTime endAt = eventPostRequest.getEndAt();
        LocalDateTime announcedAt = eventPostRequest.getAnnouncedAt();
        String imageUrl = eventPostRequest.getImageUrl();
        if (title != null) event.setTitle(title);
        if (prize != null) event.setPrize(prize);
        if (requires != null) event.setRequires(requires);
        if (startAt != null) event.setStartAt(startAt);
        if (endAt != null) event.setEndAt(endAt);
        if (announcedAt != null) event.setAnnouncedAt(announcedAt);
        if (imageUrl != null) event.setImageUrl(imageUrl);

//        event.updateEvent(eventPostRequest.getTitle(), eventPostRequest.getPrize(), eventPostRequest.getRequires(), eventPostRequest.getStartAt(),
//                eventPostRequest.getEndAt(), eventPostRequest.getAnnouncedAt(), eventPostRequest.getImageUrl());
        eventRepository.save(event);
        return toEventResponse(event);
    };

    public EventResponse getOneEvent(Long eventId) { // optional????
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트id"));
        return toEventResponse(event);
    };

    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::toEventResponse)
                .collect(Collectors.toList());
    };

    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트id"));
        List<Entry> allByEventId = entryRepository.findAllByEventId(eventId);
        for (Entry entry : allByEventId) {
            entry.setEvent(null);
        }
        eventRepository.delete(event);
    };

    private EventResponse toEventResponse(Event event) {
        return EventResponse.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .prize(event.getPrize())
                .requires(event.getRequires())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .announcedAt(event.getAnnouncedAt())
                .imageUrl(event.getImageUrl())
                .build();
    }
}
