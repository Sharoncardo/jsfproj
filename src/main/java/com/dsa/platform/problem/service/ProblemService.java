package com.dsa.platform.problem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.dsa.platform.problem.entity.Problem;
import com.dsa.platform.problem.entity.LanguageOption;
import com.dsa.platform.problem.repository.ProblemRepository;
import com.dsa.platform.problem.repository.LanguageOptionRepository;
import com.dsa.platform.problem.dto.ProblemSummaryDTO;
import com.dsa.platform.problem.dto.ProblemDetailDTO;
import com.dsa.platform.problem.dto.LanguageOptionDTO;
import com.dsa.platform.topic.service.TopicService;

@Service
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final LanguageOptionRepository languageOptionRepository;
    private final TopicService topicService;

    public ProblemService(ProblemRepository problemRepository,
                          LanguageOptionRepository languageOptionRepository,
                          TopicService topicService) {
        this.problemRepository = problemRepository;
        this.languageOptionRepository = languageOptionRepository;
        this.topicService = topicService;
    }

    public List<ProblemSummaryDTO> getProblems(String search, String topicSlug, String difficulty) {
        String s = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String t = (topicSlug != null && !topicSlug.trim().isEmpty()) ? topicSlug.trim() : null;
        String d = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty.trim() : null;

        List<Problem> problems = problemRepository.searchAndFilter(s, t, d);
        return problems.stream().map(this::toSummaryDTO).collect(Collectors.toList());
    }

    public ProblemDetailDTO getProblemDetailById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + id));
        return toDetailDTO(problem);
    }

    public ProblemDetailDTO getProblemDetailBySlug(String slug) {
        Problem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with slug: " + slug));
        return toDetailDTO(problem);
    }

    public Problem getProblemEntity(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + id));
    }

    public List<LanguageOptionDTO> getLanguageOptionsForProblem(Long problemId) {
        return languageOptionRepository.findByProblemId(problemId).stream()
                .map(this::toLanguageOptionDTO)
                .collect(Collectors.toList());
    }

    public LanguageOptionDTO getLanguageOption(Long problemId, String languageKey) {
        return languageOptionRepository.findByProblemIdAndLanguageKey(problemId, languageKey)
                .map(this::toLanguageOptionDTO)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Language option not found for problem " + problemId + " and language " + languageKey));
    }

    public ProblemSummaryDTO toSummaryDTO(Problem p) {
        int hintCount = p.getHints() != null ? p.getHints().size() : 0;
        return new ProblemSummaryDTO(
                p.getId(),
                p.getTitle(),
                p.getSlug(),
                p.getDifficulty(),
                p.getTopic() != null ? p.getTopic().getName() : "Uncategorized",
                p.getTopic() != null ? p.getTopic().getSlug() : "",
                hintCount
        );
    }

    public ProblemDetailDTO toDetailDTO(Problem p) {
        ProblemDetailDTO dto = new ProblemDetailDTO();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setSlug(p.getSlug());
        dto.setDifficulty(p.getDifficulty());
        dto.setDescription(p.getDescription());
        dto.setInputFormat(p.getInputFormat());
        dto.setOutputFormat(p.getOutputFormat());
        dto.setConstraints(p.getConstraints());
        dto.setSampleInput(p.getSampleInput());
        dto.setSampleOutput(p.getSampleOutput());
        dto.setSampleExplanation(p.getSampleExplanation());

        if (p.getTopic() != null) {
            dto.setTopic(topicService.toDTO(p.getTopic()));
        }

        List<LanguageOptionDTO> langDtos = p.getLanguageOptions().stream()
                .map(this::toLanguageOptionDTO)
                .collect(Collectors.toList());
        dto.setLanguageOptions(langDtos);

        dto.setTotalHints(p.getHints() != null ? p.getHints().size() : 0);
        return dto;
    }

    public LanguageOptionDTO toLanguageOptionDTO(LanguageOption opt) {
        return new LanguageOptionDTO(
                opt.getId(),
                opt.getName(),
                opt.getLanguageKey(),
                opt.getStarterTemplate()
        );
    }
}
