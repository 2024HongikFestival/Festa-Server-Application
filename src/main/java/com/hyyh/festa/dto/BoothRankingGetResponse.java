package com.hyyh.festa.dto;

import com.hyyh.festa.domain.booth.Booth;

public record BoothRankingGetResponse(

        Long boothId,

        String boothName,

        int likeCount

) {
    public static BoothRankingGetResponse of(Booth booth) {
        return new BoothRankingGetResponse(booth.getId() ,booth.getBoothName(), booth.getLikeCount());
    }
}
