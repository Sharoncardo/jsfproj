
package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.playbook.PlaybookResponse;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.PlaybookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playbook")
@RequiredArgsConstructor
@Tag(name = "Logic Playbook", description = "Personal DSA revision notes and attempted problems")
@SecurityRequirement(name = "bearerAuth")
public class PlaybookController {

    private final PlaybookService playbookService;

    @GetMapping
    @Operation(summary = "Get the logged-in user's Logic Playbook")
    public ResponseEntity<ApiResponse<PlaybookResponse>> getPlaybook(
            @AuthenticationPrincipal UserPrincipal principal) {

        PlaybookResponse response =
                playbookService.getPlaybook(principal.getId());

        return ResponseEntity.ok(
                ApiResponse.ok("Logic Playbook retrieved successfully", response)
        );
    }
}
