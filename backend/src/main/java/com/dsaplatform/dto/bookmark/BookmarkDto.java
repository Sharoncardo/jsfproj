package com.dsaplatform.dto.bookmark;

import com.dsaplatform.entity.Bookmark;
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
public class BookmarkDto {
    private UUID id;
    private String problemId;
    private String insight;
    private String tags;
    private Instant createdAt;

    public static BookmarkDto fromEntity(Bookmark entity) {
        return BookmarkDto.builder()
                .id(entity.getId())
                .problemId(entity.getProblemId())
                .insight(entity.getInsight())
                .tags(entity.getTags())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
