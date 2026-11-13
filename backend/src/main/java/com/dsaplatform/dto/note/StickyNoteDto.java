package com.dsaplatform.dto.note;

import com.dsaplatform.entity.StickyNote;
import com.dsaplatform.entity.enums.NoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StickyNoteDto {
    private UUID id;
    private String problemId;
    private String title;
    private String content;
    private NoteType type;
    private String color;
    private double positionX;
    private double positionY;
    private double width;
    private double height;
    private boolean pinned;
    private Instant createdAt;
    private Instant updatedAt;

    public static StickyNoteDto fromEntity(StickyNote note) {
        return StickyNoteDto.builder()
                .id(note.getId())
                .problemId(note.getProblemId())
                .title(note.getTitle())
                .content(note.getContent())
                .type(note.getType())
                .color(note.getColor())
                .positionX(note.getPositionX())
                .positionY(note.getPositionY())
                .width(note.getWidth())
                .height(note.getHeight())
                .pinned(note.isPinned())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
