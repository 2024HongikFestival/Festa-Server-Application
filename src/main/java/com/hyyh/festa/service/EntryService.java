package com.hyyh.festa.service;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.domain.Prize;
import com.hyyh.festa.dto.EntryPostRequest;
import com.hyyh.festa.dto.EntryResponse;
import com.hyyh.festa.repository.EntryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EntryService {

    private final EntryRepository entryRepository;
    private static final LocalDateTime dayFirst = LocalDateTime.of(LocalDate.of(2024, 9, 25), LocalTime.of(10, 0));
    private static final LocalDateTime daySecond = dayFirst.plusDays(1);
    private static final LocalDateTime dayThird = dayFirst.plusDays(2);
    private static final LocalDateTime due = LocalDateTime.of(LocalDate.from(dayFirst.plusDays(2)), LocalTime.of(23, 59));

    public EntryResponse createEntry(FestaUser festaUser, EntryPostRequest entryPostRequest) {
        try {
            Entry newEntry = Entry.builder()
                    .user(festaUser)
                    .name(entryPostRequest.getName())
                    .phone(entryPostRequest.getPhone())
                    .prize(Prize.valueOf(entryPostRequest.getPrize()))
                    .comment(entryPostRequest.getComment())
                    .build();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(dayFirst) && now.isBefore(daySecond)) {
                newEntry.setDate(1);
            } else if (now.isAfter(daySecond) && now.isBefore(dayThird)) {
                newEntry.setDate(2);
            } else if (now.isAfter(dayThird) && now.isBefore(due)) {
                newEntry.setDate(3);
            }

            if (!entryRepository.findAllByUserAndDate(festaUser, newEntry.getDate()).isEmpty()) {
                throw new IllegalStateException("하루에 여러 번 응모할 수 없습니다.");
            }

            Entry savedEntry = entryRepository.save(newEntry);
            return toEntryResponse(savedEntry);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
    }

    public EntryResponse getEntryById(Long entryId) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 응모id"));

        return toEntryResponse(entry);

    };

    public List<EntryResponse> getEntriesByPrize(String prize) {
        List<Entry> entries = entryRepository.findAllByPrize(Prize.valueOf(prize));
        return entries.stream()
                .map(this::toEntryResponse)
                .collect(Collectors.toList());
    }

    public EntryResponse cancelWinner(Long entryId) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 응모id"));
        if (!entry.isWinner()) {
            throw new IllegalArgumentException("이 응모는 당첨되지 않았습니다.");
        }
        entry.setWinner(false);
        entryRepository.save(entry);

        return toEntryResponse(entry);
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
