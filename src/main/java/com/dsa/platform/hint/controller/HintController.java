package com.dsa.platform.hint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dsa.platform.hint.service.HintService;
import com.dsa.platform.hint.dto.HintDTO;
import com.dsa.platform.hint.dto.HintProgressResponse;

@RestController
@RequestMapping("/api/problems/{problemId}/hints")
@CrossOrigin(origins = "*")
public class HintController {

    private final HintService hintService;

    public HintController(HintService hintService) {
        this.hintService = hintService;
    }

    /**
     * Get hint progression status for a problem.
     * Backend-controlled: Returns only hints that have been unlocked so far.
     */
    @GetMapping("/progress")
    public ResponseEntity<HintProgressResponse> getHintProgress(
            @PathVariable Long problemId,
            @RequestParam(required = false, defaultValue = "default-guest") String sessionId) {
        return ResponseEntity.ok(hintService.getHintProgress(problemId, sessionId));
    }

    /**
     * Sequentially unlock the next progressive hint level (Level 1 -> 2 -> 3 -> 4).
     * Accepts sessionId via query parameter or X-Session-Id header.
     */
    @PostMapping("/unlock-next")
    public ResponseEntity<HintProgressResponse> unlockNextHint(
            @PathVariable Long problemId,
            @RequestParam(required = false) String sessionId,
            @RequestHeader(name = "X-Session-Id", required = false) String headerSessionId) {

        String effectiveSessionId = "default-guest";
        if (sessionId != null && !sessionId.trim().isEmpty()) {
            effectiveSessionId = sessionId.trim();
        } else if (headerSessionId != null && !headerSessionId.trim().isEmpty()) {
            effectiveSessionId = headerSessionId.trim();
        }

        return ResponseEntity.ok(hintService.unlockNextHint(problemId, effectiveSessionId));
    }

    /**
     * Reset progressive hints for the current session back to locked state.
     */
    @PostMapping("/reset")
    public ResponseEntity<HintProgressResponse> resetHints(
            @PathVariable Long problemId,
            @RequestParam(required = false, defaultValue = "default-guest") String sessionId,
            @RequestHeader(name = "X-Session-Id", required = false) String headerSessionId) {

        String effectiveSessionId = (sessionId != null && !sessionId.trim().isEmpty())
                ? sessionId.trim()
                : (headerSessionId != null && !headerSessionId.trim().isEmpty() ? headerSessionId.trim() : "default-guest");

        return ResponseEntity.ok(hintService.resetHintProgress(problemId, effectiveSessionId));
    }

    /**
     * Retrieve a specific hint level, strictly verifying it has been unlocked first.
     */
    @GetMapping("/{level}")
    public ResponseEntity<HintDTO> getHintByLevel(
            @PathVariable Long problemId,
            @PathVariable int level,
            @RequestParam(required = false, defaultValue = "default-guest") String sessionId) {
        return ResponseEntity.ok(hintService.getHintByLevel(problemId, level, sessionId));
    }
}
