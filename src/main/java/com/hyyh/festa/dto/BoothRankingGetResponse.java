package com.hyyh.festa.dto;

import com.hyyh.festa.domain.Booth;

public record BoothRankingGetResponse(

        Long boothId,

        String boothName,

        int totalLike

) {
    public static BoothRankingGetResponse of(Booth booth) {
        return new BoothRankingGetResponse(booth.getId() ,booth.getBoothName(), booth.getTotalLike());
    }
}
