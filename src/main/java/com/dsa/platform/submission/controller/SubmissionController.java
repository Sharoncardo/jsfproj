package com.dsa.platform.submission.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.dsa.platform.submission.service.SubmissionService;
import com.dsa.platform.submission.dto.SubmissionRequest;
import com.dsa.platform.submission.dto.SubmissionResponse;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<SubmissionResponse> submitSolution(@Valid @RequestBody SubmissionRequest request) {
        SubmissionResponse response = submissionService.submitSolution(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByProblem(
            @PathVariable Long problemId,
            @RequestParam(required = false) String sessionId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByProblemAndSession(problemId, sessionId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }
}
