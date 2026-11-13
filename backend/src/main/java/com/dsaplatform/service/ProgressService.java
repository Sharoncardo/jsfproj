package com.dsaplatform.service;

import com.dsaplatform.dto.common.PageResponse;
import com.dsaplatform.dto.progress.ProgressSummaryResponse;
import com.dsaplatform.dto.progress.ProgressTrackDto;
import com.dsaplatform.dto.progress.RecordAttemptRequest;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProgressService {
    ProgressTrackDto recordAttempt(UUID userId, RecordAttemptRequest request);
    ProgressSummaryResponse getProgressSummary(UUID userId);
    PageResponse<ProgressTrackDto> getProgressList(UUID userId, Topic topic, ProgressStatus status, Pageable pageable);
}
