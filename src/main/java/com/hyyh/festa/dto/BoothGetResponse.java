package com.hyyh.festa.dto;

import com.hyyh.festa.domain.booth.Booth;

public record BoothGetResponse(

        Long boothId,
        String boothName

) {
    public static BoothGetResponse of(Booth booth) {
        return new BoothGetResponse(booth.getId() ,booth.getBoothName());
    }
}
