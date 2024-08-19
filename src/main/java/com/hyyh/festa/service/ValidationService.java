package com.hyyh.festa.service;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.repository.BlackListRepository;
import com.hyyh.festa.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private static final double HIU_LAT = 37.55073164533574;
    private static final double HIU_LON = 126.92549763193347;
    private static final int EARTH_RADIUS = 6371;
    private static final LocalDateTime dayFirst = LocalDateTime.of(LocalDate.of(2024, 8, 19), LocalTime.of(10, 0));
    private static final LocalDateTime daySecond = dayFirst.plusDays(1);
    private static final LocalDateTime dayThird = dayFirst.plusDays(2);
    private static final LocalDateTime due = LocalDateTime.of(LocalDate.from(dayFirst.plusDays(2)), LocalTime.of(23, 59));

    private final EntryRepository entryRepository;
    private final BlackListRepository blackListRepository;

    public boolean isEventApplicable(UserDetails festaUser) {
        LocalDateTime now = LocalDateTime.now();
        int curDay = 0;
        if (now.isAfter(dayFirst) && now.isBefore(daySecond)) {
            curDay = 1;
        } else if (now.isAfter(daySecond) && now.isBefore(dayThird)) {
            curDay = 2;
        } else if (now.isAfter(dayThird) && now.isBefore(due)) {
            curDay = 3;
        }
        List<Entry> allByUserAndDate = entryRepository.findAllByUserAndDate(festaUser, curDay);
        if (!allByUserAndDate.isEmpty()) {
            return false; // 한 유저가 같은 일차에 여러번 응모하려 하면 안됨.
        }

        return true;
    }

    public boolean isWithinArea(double latitude, double longitude, double radius) {
        double latDistance = Math.toRadians(latitude - HIU_LAT);
        double lonDistance = Math.toRadians(longitude - HIU_LON);
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