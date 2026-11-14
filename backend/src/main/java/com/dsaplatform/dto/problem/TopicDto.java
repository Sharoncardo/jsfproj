package com.dsaplatform.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private Long id;
    private String code;
    private String name;
    private String slug;
    private String description;
    private Integer displayOrder;
    private long problemCount;
}
