package com.dsaplatform.service;

import com.dsaplatform.dto.submission.SubmissionRequest;
import com.dsaplatform.dto.submission.SubmissionResponse;

import java.util.List;
import java.util.UUID;

public interface SubmissionService {

    SubmissionResponse submitSolution(UUID userId, SubmissionRequest request);

    List<SubmissionResponse> getSubmissionsByProblem(UUID userId, Long problemId);

    SubmissionResponse getSubmissionById(UUID userId, Long id);
}
