package com.hyyh.festa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListRequestDTO {
    @NotNull(message = "userId는 반드시 필요합니다.")
    private String userId;
}
