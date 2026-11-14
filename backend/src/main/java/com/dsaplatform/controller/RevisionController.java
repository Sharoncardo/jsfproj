
package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.revision.ProblemRevisionDto;
import com.dsaplatform.dto.revision.SaveRevisionRequest;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.RevisionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems/{problemId}/revision")
@RequiredArgsConstructor
@Tag(name = "Problem Revision", description = "Personal DSA revision notes")
@SecurityRequirement(name = "bearerAuth")
public class RevisionController {

    private final RevisionService revisionService;

    @GetMapping
    @Operation(summary = "Get personal revision notes for a problem")
    public ResponseEntity<ApiResponse<ProblemRevisionDto>> getRevision(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long problemId) {

        ProblemRevisionDto response =
                revisionService.getRevision(principal.getId(), problemId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping
    @Operation(summary = "Save or update personal revision notes")
    public ResponseEntity<ApiResponse<ProblemRevisionDto>> saveRevision(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long problemId,
            @Valid @RequestBody SaveRevisionRequest request) {

        ProblemRevisionDto response =
                revisionService.saveRevision(
                        principal.getId(), problemId, request);

        return ResponseEntity.ok(
                ApiResponse.ok("Revision notes saved successfully", response));
    }
}
