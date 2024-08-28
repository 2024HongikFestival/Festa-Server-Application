package com.hyyh.festa.service;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.domain.Prize;
import com.hyyh.festa.dto.EntryResponse;
import com.hyyh.festa.repository.EntryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrizeService {

    private final EntryRepository entryRepository;

    public List<EntryResponse> drawAllWinners(String prize) {
        Prize prizeEnum;
        try {
            prizeEnum = Prize.valueOf(prize);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 경품입니다.");
        }
        List<Entry> entries = entryRepository.findAllByPrize(prizeEnum);

        if (entries.stream().anyMatch(Entry::isWinner)) {
            throw new IllegalArgumentException("이미 당첨자가 한 명 이상 존재하여 전체 추첨을 할 수 없습니다.");
        }
        if (entries.size() < prizeEnum.quantity) {
            throw new IllegalArgumentException("응모자 수가 경품 수량보다 적습니다.");
        }

        return drawWinners(entries, prizeEnum.quantity);
    }

    public EntryResponse drawOneWinner(String prize) {
        Prize prizeEnum;
        try {
            prizeEnum = Prize.valueOf(prize);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 경품입니다.");
        }

        List<Entry> allEntries = entryRepository.findAllByPrize(prizeEnum);

        if (allEntries.stream().filter(Entry::isWinner).count() == prizeEnum.quantity) {
            throw new IllegalArgumentException("이미 전체 추첨되었습니다.");
        }

        List<Entry> notWinnerEntries = allEntries.stream()
                .filter(entry -> !entry.isWinner())
                .collect(Collectors.toList());
        if (notWinnerEntries.isEmpty()) {
            throw new IllegalArgumentException("추첨 가능한 응모자가 없습니다.");
        }

        return drawWinners(notWinnerEntries, 1).get(0);
    }


    private List<EntryResponse> drawWinners(List<Entry> entries, int quantity) {
        List<EntryResponse> winners = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < quantity; i++) {
            int randomIndex = random.nextInt(entries.size());
            Entry winner = entries.remove(randomIndex);
            winner.setWinner(true);
            entryRepository.save(winner);
            winners.add(toEntryResponse(winner));
        }
        return winners;
    }

    private EntryResponse toEntryResponse(Entry entry) {
        return EntryResponse.builder()
                .entryId(entry.getId())
                .name(entry.getName())
                .phone(entry.getPhone())
                .prize(String.valueOf(entry.getPrize()))
                .comment(entry.getComment())
                .date(entry.getDate())
                .isWinner(entry.isWinner())
                .build();
    }
}
