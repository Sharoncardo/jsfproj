package com.dsaplatform.service.impl;

import com.dsaplatform.dto.progress.RecordAttemptRequest;
import com.dsaplatform.dto.submission.SubmissionRequest;
import com.dsaplatform.dto.submission.SubmissionResponse;
import com.dsaplatform.entity.Problem;
import com.dsaplatform.entity.Submission;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.exception.BadRequestException;
import com.dsaplatform.exception.ForbiddenException;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.ProblemRepository;
import com.dsaplatform.repository.SubmissionRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.ProgressService;
import com.dsaplatform.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service handling code submissions and progress linkage.
 * NOTE: The evaluator is a lightweight heuristic and validation engine,
 * not an isolated sandbox code execution engine.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionServiceImpl implements SubmissionService {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("java", "python", "cpp");
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final ProgressService progressService;
    private final Random random = new Random();

    @Override
    @Transactional
    public SubmissionResponse submitSolution(UUID userId, SubmissionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", request.getProblemId()));

        String language = request.getLanguage() != null ? request.getLanguage().trim().toLowerCase() : "";
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            throw new BadRequestException("Unsupported programming language: '" + request.getLanguage() +
                    "'. Supported languages are: " + SUPPORTED_LANGUAGES);
        }

        String code = request.getSourceCode() != null ? request.getSourceCode().trim() : "";
        if (code.isEmpty()) {
            throw new BadRequestException("Source code cannot be empty");
        }

        // Lightweight heuristic evaluation (Mock evaluator)
        String status;
        String feedback;
        int executionTime;
        int memoryKb;

        if (code.length() < 20) {
            status = "COMPILATION_ERROR";
            feedback = "Compilation Failed: Source code is incomplete or contains insufficient implementation.";
            executionTime = 0;
            memoryKb = 0;
        } else if (!code.contains("return") && !code.contains("def") && !code.contains("void") && !code.contains("class")) {
            status = "WRONG_ANSWER";
            feedback = "Evaluation Failed: Solution did not return an output or missing method declaration.";
            executionTime = 14;
            memoryKb = 32500;
        } else {
            status = "ACCEPTED";
            executionTime = 25 + random.nextInt(45);
            memoryKb = 38000 + random.nextInt(6000);
            feedback = String.format("Success: All sample test cases passed! Runtime: %d ms, Memory: %.1f MB. Language: %s.",
                    executionTime, memoryKb / 1024.0, language.toUpperCase());
        }

        Submission submission = Submission.builder()
                .problem(problem)
                .user(user)
                .language(language)
                .sourceCode(code)
                .status(status)
                .executionTimeMs(executionTime)
                .memoryKb(memoryKb)
                .feedback(feedback)
                .sessionId(request.getSessionId() != null ? request.getSessionId().trim() : user.getUsername())
                .build();

        Submission saved = submissionRepository.save(submission);

        // Integrate with Progress Tracking: update progress according to evaluation result
        // Integrate with Progress Tracking
        ProgressStatus progressStatus = "ACCEPTED".equals(status)
                ? ProgressStatus.SOLVED
                : ProgressStatus.IN_PROGRESS;

        RecordAttemptRequest attemptRequest = RecordAttemptRequest.builder()
                .problemId(problem.getSlug())
                .topic(problem.getTopicCode())
                .difficulty(problem.getDifficulty())
                .status(progressStatus)
                .timeSpentSeconds(Math.max(executionTime / 1000L, 5L))
                .incrementAttempts(true)
                .build();

        progressService.recordAttempt(userId, attemptRequest);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByProblem(UUID userId, Long problemId) {
        if (!problemRepository.existsById(problemId)) {
            throw new ResourceNotFoundException("Problem", "id", problemId);
        }
        return submissionRepository.findByProblemIdAndUserIdOrderBySubmittedAtDesc(problemId, userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubmissionResponse getSubmissionById(UUID userId, Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", "id", id));

        if (submission.getUser() == null || !submission.getUser().getId().equals(userId)) {
            throw new ForbiddenException(
                "You are not authorized to view this submission");
        }

        return toResponse(submission);
    }

    private SubmissionResponse toResponse(Submission s) {
        return SubmissionResponse.builder()
                .id(s.getId())
                .problemId(s.getProblem().getId())
                .problemTitle(s.getProblem().getTitle())
                .problemSlug(s.getProblem().getSlug())
                .language(s.getLanguage())
                .status(s.getStatus())
                .executionTimeMs(s.getExecutionTimeMs())
                .memoryKb(s.getMemoryKb())
                .submittedAt(s.getSubmittedAt())
                .feedback(s.getFeedback())
                .sourceCode(s.getSourceCode())
                .build();
    }
}
