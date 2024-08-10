package com.hyyh.festa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidationService {
    public boolean isEventApplicable(Long eventId) {
        return true;
    }

    public boolean isWithinArea(double latitude, double longtitude) {
        return true;
    }

    public boolean isUserBlacklist(String username){
        return false;
    }
}

