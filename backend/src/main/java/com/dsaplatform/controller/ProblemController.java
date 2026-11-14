
package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.problem.ProblemDetailDto;
import com.dsaplatform.dto.problem.ProblemSummaryDto;
import com.dsaplatform.dto.problem.TopicDto;
import com.dsaplatform.service.ProblemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Tag(name = "Problems", description = "DSA problem catalogue and details")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/topics")
    @Operation(summary = "Get all available DSA topics")
    public ResponseEntity<ApiResponse<List<TopicDto>>> getAllTopics() {
        List<TopicDto> topics = problemService.getAllTopics();
        return ResponseEntity.ok(ApiResponse.ok(topics));
    }

    @GetMapping
    @Operation(summary = "Get problems with optional search and filters")
    public ResponseEntity<ApiResponse<List<ProblemSummaryDto>>> getProblems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {

        List<ProblemSummaryDto> problems =
                problemService.getProblems(search, topic, difficulty);

        return ResponseEntity.ok(ApiResponse.ok(problems));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get problem details by ID")
    public ResponseEntity<ApiResponse<ProblemDetailDto>> getProblemById(
            @PathVariable Long id) {

        ProblemDetailDto problem = problemService.getProblemById(id);
        return ResponseEntity.ok(ApiResponse.ok(problem));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get problem details by slug")
    public ResponseEntity<ApiResponse<ProblemDetailDto>> getProblemBySlug(
            @PathVariable String slug) {

        ProblemDetailDto problem = problemService.getProblemBySlug(slug);
        return ResponseEntity.ok(ApiResponse.ok(problem));
    }
}
