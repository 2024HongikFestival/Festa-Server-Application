package com.hyyh.festa.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyyh.festa.domain.Pub;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class PubRepository {
    private static final ConcurrentHashMap<Integer, Pub> pubs = new ConcurrentHashMap<Integer, Pub>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void initializePub() {
        pubs.put(1, new Pub(1, 0));
        pubs.put(2, new Pub(2, 0));
        pubs.put(3, new Pub(3, 0));
    }
    public void plusLike(Integer pubId) {
        Pub pub = pubs.get(pubId);
        pub.plusLike();
        pubs.put(pubId, pub);
    }

    public String getAllPubs() {
        try {
            List<Pub> pubList = new ArrayList<>(pubs.values());
            return objectMapper.writeValueAsString(pubList);
        } catch (JsonProcessingException e) {
            // Exception handling (you might want to log the error)
            e.printStackTrace();
            return "Error converting to JSON";
        }
    }
}
