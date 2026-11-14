package com.dsaplatform.service;

import com.dsaplatform.dto.revision.ProblemRevisionDto;
import com.dsaplatform.dto.revision.SaveRevisionRequest;

import java.util.UUID;

public interface RevisionService {

    ProblemRevisionDto getRevision(UUID userId, Long problemId);

    ProblemRevisionDto saveRevision(UUID userId, Long problemId, SaveRevisionRequest request);
}
