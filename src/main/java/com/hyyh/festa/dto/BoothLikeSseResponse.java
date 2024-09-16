package com.hyyh.festa.dto;

import com.hyyh.festa.domain.Booth;

public record BoothLikeSseResponse(

        Long boothId,

        int totalLike

) {
    public static BoothLikeSseResponse of(Booth booth) {
        return new BoothLikeSseResponse(
                booth.getId(),
                booth.getTotalLike());
    }
}
