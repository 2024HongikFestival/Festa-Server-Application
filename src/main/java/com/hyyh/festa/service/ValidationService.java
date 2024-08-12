package com.hyyh.festa.service;

import com.hyyh.festa.domain.Event;
import com.hyyh.festa.repository.BlackListRepository;
import com.hyyh.festa.repository.EntryRepository;
import com.hyyh.festa.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private static final double HIU_LAT = 37.55073164533574;
    private static final double HIU_LON = 126.92549763193347;
    private static final int EARTH_RADIUS = 6371;

    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;
    private final BlackListRepository blackListRepository;

    public char isEventApplicable(Long eventId, UserDetails festaUser) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return 'n'; // 없는 이벤트면 false
        }

        boolean hasAlreadyApplied = entryRepository.existsByUserAndEvent(festaUser, event);
        if (hasAlreadyApplied) {
            return 'd'; // 한 유저가 같은 이벤트에 또 응모하려 하면 false
        }

        return 'g';
    }

    public boolean isWithinArea(double latitude, double longtitude, double radius) {
        double latDistance = Math.toRadians(latitude - HIU_LAT);
        double lonDistance = Math.toRadians(longtitude - HIU_LON);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(HIU_LAT)) * Math.cos(Math.toRadians(latitude)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance <= radius;
    }

    public boolean isUserBlacklist(String username){
        return blackListRepository.existsByFestaUserKakaoSub(username);
    }
}

