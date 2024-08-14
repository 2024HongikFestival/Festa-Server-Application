package com.hyyh.festa.dto;

import com.hyyh.festa.domain.Booth;

public record BoothLikeSseResponse(

        Long boothId,

        String boothName,

        int totalLike,

        int increasedLike

) {
    public static BoothLikeSseResponse of(Booth booth) {
        return new BoothLikeSseResponse(
                booth.getId(),
                booth.getBoothName(),
                booth.getTotalLike(),
                booth.getTotalLike() - booth.getPrevioudLike());
    }
}
