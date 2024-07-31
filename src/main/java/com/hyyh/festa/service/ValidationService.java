package com.hyyh.festa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidationService {

    //private final EventRepository eventRepository;
    //private final BlackListRepository blackListRepository;
    public boolean isEventApplicable(Long eventId) {
        /*

        // 존재하는 이벤트인지 확인
        if (!eventRepository.existsByIdAndDeletedFalse(eventId)) {
            return false;
        }
        // 응모 기간 확인
        Event event = eventRepository.findById(eventId).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(event.getStartAt()) && now.isBefore(event.getEndAt())) {
            return true;
        }
        return false;
         */

        return true;
    }

    public boolean isWithinArea(double latitude, double longtitude) {
        return true;
    }

    public boolean isUserBlacklist(String username){
        //return blacklistRepository.findByUsername(username).isPresent();
        return true;
    }
}

