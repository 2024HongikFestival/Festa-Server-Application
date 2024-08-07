package com.hyyh.festa.dto;

import com.hyyh.festa.domain.booth.Booth;

public record BoothLikeSseResponse(

        Long boothId,

        String boothName,

        int likeCount

) {
    public static BoothLikeSseResponse of(Booth booth) {
        return new BoothLikeSseResponse(booth.getId() ,booth.getBoothName(), booth.getLikeCount());
    }
}
