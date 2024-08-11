package com.hyyh.festa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryPostRequest {
    @NotBlank(message = "이름은 반드시 필요합니다.")
    @Size(max = 10, message = "이름은 최대 10글자까지 입력할 수 있습니다.")
    private String name;

    @NotBlank(message = "전화번호는 반드시 필요합니다.")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호의 형식은 000-0000-0000과 같아야 합니다.")
    private String phone;

    @Size(max = 500, message = "코멘트는 최대 500글자까지 입력할 수 있습니다.")
    private String comment;
}
