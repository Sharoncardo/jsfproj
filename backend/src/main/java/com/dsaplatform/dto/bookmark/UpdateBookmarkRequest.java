package com.dsaplatform.dto.bookmark;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookmarkRequest {

    private String insight;

    @Size(max = 255, message = "Tags cannot exceed 255 characters")
    private String tags;
}
