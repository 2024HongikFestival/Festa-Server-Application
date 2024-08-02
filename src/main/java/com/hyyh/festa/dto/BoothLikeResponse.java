package com.hyyh.festa.dto;

import com.hyyh.festa.domain.booth.Booth;

public record BoothLikeResponse(

        Long boothId,
        String boothName

) {
    public static BoothLikeResponse of(Booth booth) {
        return new BoothLikeResponse(booth.getId() ,booth.getBoothName());
    }
}
