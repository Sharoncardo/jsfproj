package com.dsaplatform.dto.note;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePositionRequest {

    @NotNull(message = "positionX is required")
    private Double positionX;

    @NotNull(message = "positionY is required")
    private Double positionY;

    private Double width;

    private Double height;
}
