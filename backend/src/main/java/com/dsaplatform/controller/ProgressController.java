package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.common.PageResponse;
import com.dsaplatform.dto.progress.ProgressSummaryResponse;
import com.dsaplatform.dto.progress.ProgressTrackDto;
import com.dsaplatform.dto.progress.RecordAttemptRequest;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Tag(name = "Progress Tracking", description = "Endpoints for tracking DSA problem attempts, streaks, and analytics")
@SecurityRequirement(name = "bearerAuth")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping
    @Operation(summary = "Record or update a problem attempt/solution")
    public ResponseEntity<ApiResponse<ProgressTrackDto>> recordAttempt(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody RecordAttemptRequest request) {
        ProgressTrackDto dto = progressService.recordAttempt(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Progress recorded successfully", dto));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get aggregated progress summary including streak, topic percentages, and 30-day activity")
    public ResponseEntity<ApiResponse<ProgressSummaryResponse>> getSummary(
            @AuthenticationPrincipal UserPrincipal principal) {
        ProgressSummaryResponse summary = progressService.getProgressSummary(principal.getId());
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    @GetMapping
    @Operation(summary = "List individual problem progress history with optional topic/status filter and pagination")
    public ResponseEntity<ApiResponse<PageResponse<ProgressTrackDto>>> getProgressList(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Topic topic,
            @RequestParam(required = false) ProgressStatus status,
            @PageableDefault(size = 20, sort = "lastAttemptedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ProgressTrackDto> list = progressService.getProgressList(principal.getId(), topic, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}
