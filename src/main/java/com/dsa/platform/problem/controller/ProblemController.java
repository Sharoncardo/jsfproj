package com.dsa.platform.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.dsa.platform.problem.service.ProblemService;
import com.dsa.platform.problem.dto.ProblemSummaryDTO;
import com.dsa.platform.problem.dto.ProblemDetailDTO;
import com.dsa.platform.problem.dto.LanguageOptionDTO;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "*")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<List<ProblemSummaryDTO>> getProblems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {
        return ResponseEntity.ok(problemService.getProblems(search, topic, difficulty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDetailDTO> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemDetailById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProblemDetailDTO> getProblemBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(problemService.getProblemDetailBySlug(slug));
    }

    @GetMapping("/{id}/languages")
    public ResponseEntity<List<LanguageOptionDTO>> getLanguages(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getLanguageOptionsForProblem(id));
    }

    @GetMapping("/{id}/languages/{languageKey}")
    public ResponseEntity<LanguageOptionDTO> getLanguageOption(
            @PathVariable Long id,
            @PathVariable String languageKey) {
        return ResponseEntity.ok(problemService.getLanguageOption(id, languageKey));
    }
}
