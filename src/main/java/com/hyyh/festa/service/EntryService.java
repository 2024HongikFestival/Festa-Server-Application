package com.hyyh.festa.service;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.domain.Event;
import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.dto.EntryPostRequest;
import com.hyyh.festa.dto.EntryResponse;
import com.hyyh.festa.repository.EntryRepository;
import com.hyyh.festa.repository.EventRepository;
import com.hyyh.festa.repository.FestaUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryService {

    private final EntryRepository entryRepository;
    private final EventRepository eventRepository;
    private final FestaUserRepository festaUserRepository;

    public EntryResponse createEntry(Long eventId, FestaUser festaUser, EntryPostRequest entryPostRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트id"));

        Entry newEntry = Entry.builder()
                .event(event)
                .user(festaUser)
                .name(entryPostRequest.getName())
                .phone(entryPostRequest.getPhone())
                .comment(entryPostRequest.getComment())
                .build();

        Entry savedEntry = entryRepository.save(newEntry);

        return toEntryResponse(savedEntry);
    }

    public EntryResponse getEntryById(Long eventId, Long entryId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트id"));
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 응모id"));

        if (!entry.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException("해당 이벤트에 속하지 않는 응모id");
        }

        return toEntryResponse(entry);

    };

    public List<EntryResponse> getEntriesByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트id"));
        List<Entry> entries = entryRepository.findAllByEventId(eventId);
        return entries.stream()
                .map(this::toEntryResponse)
                .collect(Collectors.toList());
    }

    private EntryResponse toEntryResponse(Entry entry) {
        return EntryResponse.builder()
                .entryId(entry.getId())
                .eventId(entry.getEvent().getId())
                .name(entry.getName())
                .phone(entry.getPhone())
                .comment(entry.getComment())
                .build();
    }
}
