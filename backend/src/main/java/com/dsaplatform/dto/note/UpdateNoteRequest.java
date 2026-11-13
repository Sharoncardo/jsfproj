package com.dsaplatform.dto.note;

import com.dsaplatform.entity.enums.NoteType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {

    @Size(max = 120, message = "Title cannot exceed 120 characters")
    private String title;

    private String content;

    private NoteType type;

    private String color;

    private Boolean pinned;
}
