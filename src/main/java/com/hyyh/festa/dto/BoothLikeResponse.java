package com.hyyh.festa.dto;

import com.hyyh.festa.domain.booth.Booth;

public record BoothLikeResponse(
        String boothName,
        int likeCount

) {
    public static BoothLikeResponse of(Booth booth) {
        return new BoothLikeResponse(booth.getBoothName(), booth.getLikeCount());
    }
}
