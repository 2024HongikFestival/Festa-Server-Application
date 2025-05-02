package com.hyyh.festa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LostRequestDTO {

    @NotBlank(message = "발견 위치는 반드시 필요합니다.")
    @Size(max = 50, message = "발견 위치는 최대 50글자까지 입력할 수 있습니다.")
    private String foundLocation;

    @NotBlank(message = "보관 위치는 반드시 필요합니다.")
    @Size(max = 50, message = "보관 위치는 최대 50글자까지 입력할 수 있습니다.")
    private String storageLocation;

    @Size(max = 100, message = "설명은 최대 100글자까지 입력할 수 있습니다.")
    private String content;

    @NotBlank(message = "사진은 반드시 필요합니다.")
    private String imageUrl;
}
