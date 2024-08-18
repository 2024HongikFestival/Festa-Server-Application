package com.hyyh.festa.dto;

import java.util.List;

public record LostPageResponse(
        int currentPage,
        int totalPage,
        List<?> losts
) {
}
