package com.dsaplatform.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageOptionDto {
    private Long id;
    private String name;
    private String languageKey;
    private String starterTemplate;
}
