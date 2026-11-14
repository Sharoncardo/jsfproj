package com.dsaplatform.service.impl;

import com.dsaplatform.dto.problem.LanguageOptionDto;
import com.dsaplatform.dto.problem.ProblemDetailDto;
import com.dsaplatform.dto.problem.ProblemSummaryDto;
import com.dsaplatform.dto.problem.TopicDto;
import com.dsaplatform.entity.Problem;
import com.dsaplatform.entity.TopicEntity;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.Topic;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.LanguageOptionRepository;
import com.dsaplatform.repository.ProblemRepository;
import com.dsaplatform.repository.TopicRepository;
import com.dsaplatform.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;
    private final LanguageOptionRepository languageOptionRepository;

    @Override
    public List<TopicDto> getAllTopics() {
        List<TopicEntity> topics = topicRepository.findAllByOrderByDisplayOrderAsc();
        List<Problem> allProblems = problemRepository.findAll();

        Map<String, Long> problemCountsByCode = allProblems.stream()
                .collect(Collectors.groupingBy(p -> p.getTopicCode().name(), Collectors.counting()));

        return topics.stream()
                .map(t -> TopicDto.builder()
                        .id(t.getId())
                        .code(t.getCode())
                        .name(t.getName())
                        .slug(t.getSlug())
                        .description(t.getDescription())
                        .displayOrder(t.getDisplayOrder())
                        .problemCount(problemCountsByCode.getOrDefault(t.getCode(), 0L))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProblemSummaryDto> getProblems(String search, String topicStr, String difficultyStr) {
        Topic topicEnum = parseTopic(topicStr);
        Difficulty difficultyEnum = parseDifficulty(difficultyStr);

        List<Problem> problems = problemRepository.findWithFilters(
                (search != null && !search.trim().isEmpty()) ? search.trim() : null,
                topicEnum,
                difficultyEnum
        );

        return problems.stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProblemDetailDto getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", id));
        return toDetailDto(problem);
    }

    @Override
    public ProblemDetailDto getProblemBySlug(String slug) {
        Problem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "slug", slug));
        return toDetailDto(problem);
    }

    private ProblemSummaryDto toSummaryDto(Problem p) {
        String snippet = p.getDescription();
        if (snippet != null && snippet.length() > 140) {
            snippet = snippet.substring(0, 137) + "...";
        }
        return ProblemSummaryDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .difficulty(p.getDifficulty())
                .leetcodeNumber(p.getLeetcodeNumber())
                .topicCode(p.getTopicCode().name())
                .topicName(p.getTopic().getName())
                .topicSlug(p.getTopic().getSlug())
                .descriptionSnippet(snippet)
                .build();
    }

    private ProblemDetailDto toDetailDto(Problem p) {
        List<LanguageOptionDto> langDtos = languageOptionRepository.findByProblemId(p.getId()).stream()
                .map(l -> LanguageOptionDto.builder()
                        .id(l.getId())
                        .name(l.getName())
                        .languageKey(l.getLanguageKey())
                        .starterTemplate(l.getStarterTemplate())
                        .build())
                .collect(Collectors.toList());

        return ProblemDetailDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .difficulty(p.getDifficulty())
                .leetcodeNumber(p.getLeetcodeNumber())
                .topicCode(p.getTopicCode().name())
                .topicName(p.getTopic().getName())
                .topicSlug(p.getTopic().getSlug())
                .description(p.getDescription())
                .inputFormat(p.getInputFormat())
                .outputFormat(p.getOutputFormat())
                .constraints(p.getConstraints())
                .sampleInput(p.getSampleInput())
                .sampleOutput(p.getSampleOutput())
                .sampleExplanation(p.getSampleExplanation())
                .languageOptions(langDtos)
                .totalHints(4)
                .build();
    }

    private Topic parseTopic(String topicStr) {
        if (topicStr == null || topicStr.trim().isEmpty()) {
            return null;
        }
        String clean = topicStr.trim();
        // Try direct enum match
        try {
            return Topic.valueOf(clean.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {}

        // Try lookup by slug in topics table
        return topicRepository.findBySlug(clean)
                .map(t -> {
                    try {
                        return Topic.valueOf(t.getCode());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    private Difficulty parseDifficulty(String diffStr) {
        if (diffStr == null || diffStr.trim().isEmpty()) {
            return null;
        }
        try {
            return Difficulty.valueOf(diffStr.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
