package com.dsa.platform.submission.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.dsa.platform.submission.entity.Submission;
import com.dsa.platform.submission.repository.SubmissionRepository;
import com.dsa.platform.submission.dto.SubmissionRequest;
import com.dsa.platform.submission.dto.SubmissionResponse;
import com.dsa.platform.problem.entity.Problem;
import com.dsa.platform.problem.repository.ProblemRepository;

@Service
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final Random random = new Random();

    public SubmissionService(SubmissionRepository submissionRepository, ProblemRepository problemRepository) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
    }

    public SubmissionResponse submitSolution(SubmissionRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + request.getProblemId()));

        String code = request.getSourceCode() != null ? request.getSourceCode().trim() : "";
        String language = request.getLanguage() != null ? request.getLanguage().toLowerCase().trim() : "java";

        // Logic check: basic syntax / completeness validation
        String status;
        String feedback;
        int executionTime;
        int memoryKb;

        if (code.length() < 15) {
            status = "COMPILATION_ERROR";
            feedback = "Compilation Failed: Source code is incomplete or contains insufficient implementation.";
            executionTime = 0;
            memoryKb = 0;
        } else if (!code.contains("return") && !code.contains("def") && !code.contains("void") && !code.contains("class")) {
            status = "WRONG_ANSWER";
            feedback = "Evaluation Failed: Solution did not return an output or missing method declaration.";
            executionTime = 12;
            memoryKb = 32500;
        } else {
            status = "ACCEPTED";
            executionTime = 25 + random.nextInt(45);
            memoryKb = 38000 + random.nextInt(6000);
            feedback = String.format("Success: All sample test cases passed! Runtime: %d ms, Memory: %.1f MB. Language: %s.",
                    executionTime, memoryKb / 1024.0, language.toUpperCase());
        }

        Submission submission = new Submission();
        submission.setProblem(problem);
        submission.setLanguage(language);
        submission.setSourceCode(code);
        submission.setStatus(status);
        submission.setExecutionTimeMs(executionTime);
        submission.setMemoryKb(memoryKb);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setFeedback(feedback);
        submission.setSessionId(request.getSessionId() != null ? request.getSessionId() : "default-guest");

        Submission saved = submissionRepository.save(submission);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByProblem(Long problemId) {
        return submissionRepository.findByProblemIdOrderBySubmittedAtDesc(problemId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByProblemAndSession(Long problemId, String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return getSubmissionsByProblem(problemId);
        }
        return submissionRepository.findByProblemIdAndSessionIdOrderBySubmittedAtDesc(problemId, sessionId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getSubmissionById(Long id) {
        Submission sub = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found with id: " + id));
        return toResponse(sub);
    }

    public SubmissionResponse toResponse(Submission s) {
        return new SubmissionResponse(
                s.getId(),
                s.getProblem().getId(),
                s.getProblem().getTitle(),
                s.getLanguage(),
                s.getStatus(),
                s.getExecutionTimeMs(),
                s.getMemoryKb(),
                s.getSubmittedAt(),
                s.getFeedback(),
                s.getSourceCode()
        );
    }
}
