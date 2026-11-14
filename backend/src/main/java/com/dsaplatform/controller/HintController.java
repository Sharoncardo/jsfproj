
package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.hint.HintProgressDto;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.HintService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems/{problemId}/hints")
@RequiredArgsConstructor
@Tag(name = "Hints", description = "Progressive hint unlocking for DSA problems")
@SecurityRequirement(name = "bearerAuth")
public class HintController {

    private final HintService hintService;

    @GetMapping
    @Operation(summary = "Get hint progress for a problem")
    public ResponseEntity<ApiResponse<HintProgressDto>> getHintProgress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long problemId) {

        HintProgressDto response =
                hintService.getHintProgress(principal.getId(), problemId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/unlock")
    @Operation(summary = "Unlock the next progressive hint")
    public ResponseEntity<ApiResponse<HintProgressDto>> unlockNextHint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long problemId) {

        HintProgressDto response =
                hintService.unlockNextHint(principal.getId(), problemId);

        return ResponseEntity.ok(
                ApiResponse.ok("Next hint unlocked successfully", response));
    }
}
