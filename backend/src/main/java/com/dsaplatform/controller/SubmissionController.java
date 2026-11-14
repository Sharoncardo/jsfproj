
package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.submission.SubmissionRequest;
import com.dsaplatform.dto.submission.SubmissionResponse;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.SubmissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Tag(name = "Submissions", description = "Code submission and submission history")
@SecurityRequirement(name = "bearerAuth")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    @Operation(summary = "Submit a solution")
    public ResponseEntity<ApiResponse<SubmissionResponse>> submitSolution(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SubmissionRequest request) {

        SubmissionResponse response =
                submissionService.submitSolution(principal.getId(), request);

        return ResponseEntity.ok(
                ApiResponse.ok("Solution submitted successfully", response));
    }

    @GetMapping("/problem/{problemId}")
    @Operation(summary = "Get all submissions for a problem")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getSubmissionsByProblem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long problemId) {

        List<SubmissionResponse> response =
                submissionService.getSubmissionsByProblem(
                        principal.getId(), problemId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a submission by ID")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmissionById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        SubmissionResponse response =
                submissionService.getSubmissionById(principal.getId(), id);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
