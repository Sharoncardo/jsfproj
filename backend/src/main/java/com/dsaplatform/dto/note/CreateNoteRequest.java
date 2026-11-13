package com.dsaplatform.dto.note;

import com.dsaplatform.entity.enums.NoteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequest {

    private String problemId;

    @NotBlank(message = "Title is required")
    @Size(max = 120, message = "Title cannot exceed 120 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Type is required")
    @Builder.Default
    private NoteType type = NoteType.NOTE;

    @Builder.Default
    private String color = "#FEF08A";

    @Builder.Default
    private Double positionX = 100.0;

    @Builder.Default
    private Double positionY = 100.0;

    @Builder.Default
    private Double width = 280.0;

    @Builder.Default
    private Double height = 240.0;

    @Builder.Default
    private Boolean pinned = false;
}
