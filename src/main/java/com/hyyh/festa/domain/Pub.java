package com.hyyh.festa.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pub {

    public Integer pubId;
    public Integer likeCount;

    public Pub(Integer pubId, Integer likeCount) {
        this.pubId = pubId;
        this.likeCount = likeCount;
    }

    public void plusLike() {
        this.likeCount += 1;
    }
}

