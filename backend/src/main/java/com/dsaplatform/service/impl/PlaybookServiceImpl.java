
package com.dsaplatform.service.impl;

import com.dsaplatform.dto.hint.HintDto;
import com.dsaplatform.dto.playbook.PatternPlaybookDto;
import com.dsaplatform.dto.playbook.PlaybookProblemDetailDto;
import com.dsaplatform.dto.playbook.PlaybookResponse;
import com.dsaplatform.entity.Hint;
import com.dsaplatform.entity.Problem;
import com.dsaplatform.entity.ProgressTrack;
import com.dsaplatform.entity.TopicEntity;
import com.dsaplatform.entity.UserHintProgress;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.repository.HintRepository;
import com.dsaplatform.repository.ProblemRepository;
import com.dsaplatform.repository.ProgressTrackRepository;
import com.dsaplatform.repository.TopicRepository;
import com.dsaplatform.repository.UserHintProgressRepository;
import com.dsaplatform.service.PlaybookService;
import com.dsaplatform.service.RevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaybookServiceImpl implements PlaybookService {

    private final ProgressTrackRepository progressTrackRepository;
    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;
    private final HintRepository hintRepository;
    private final UserHintProgressRepository userHintProgressRepository;
    private final RevisionService revisionService;

    @Override
    public PlaybookResponse getPlaybook(UUID userId) {

        // 1. Get this user's progress records.
        List<ProgressTrack> progressRecords =
                progressTrackRepository.findAllByUserId(userId);

        // 2. Keep only attempted or solved problems.
        List<ProgressTrack> eligibleProgress = progressRecords.stream()
                .filter(progress ->
                        progress.getStatus() == ProgressStatus.IN_PROGRESS
                        || progress.getStatus() == ProgressStatus.SOLVED)
                .collect(Collectors.toList());

        // 3. Retrieve patterns in their configured display order.
        List<TopicEntity> topics =
                topicRepository.findAllByOrderByDisplayOrderAsc();

        // 4. Prepare a group for each pattern.
        Map<String, PatternPlaybookDto> patternMap = new LinkedHashMap<>();

        for (TopicEntity topic : topics) {
            patternMap.put(
                    topic.getCode(),
                    PatternPlaybookDto.builder()
                            .topicCode(topic.getCode())
                            .topicName(topic.getName())
                            .topicSlug(topic.getSlug())
                            .description(topic.getDescription())
                            .problemCount(0)
                            .problems(new ArrayList<>())
                            .build()
            );
        }

        int totalProblems = 0;

        // 5. Match progress records to catalogue problems using slugs.
        for (ProgressTrack progress : eligibleProgress) {

            Problem problem = problemRepository
                    .findBySlug(progress.getProblemId())
                    .orElse(null);

            // Ignore progress records that have no matching catalogue entry.
            if (problem == null || problem.getTopic() == null) {
                continue;
            }

            String topicCode = problem.getTopic().getCode();

            PatternPlaybookDto pattern = patternMap.get(topicCode);

            // Ignore problems whose pattern is not in the catalogue.
            if (pattern == null) {
                continue;
            }

            // 6. Get the student's unlocked hint level.
            int unlockedLevel = userHintProgressRepository
                    .findByUserIdAndProblemId(userId, problem.getId())
                    .map(UserHintProgress::getUnlockedLevel)
                    .orElse(0);

            // 7. Retrieve all hints, including locked hints.
            List<HintDto> hints = hintRepository
                    .findByProblemIdOrderByLevelAsc(problem.getId())
                    .stream()
                    .map(hint -> mapHint(hint, unlockedLevel))
                    .collect(Collectors.toList());

            // 8. Retrieve this student's personal revision.
            var revision = revisionService.getRevision(
                    userId,
                    problem.getId()
            );

            // 9. Build the problem's revision details.
            PlaybookProblemDetailDto problemDetail =
                    PlaybookProblemDetailDto.builder()
                            .problemId(problem.getId())
                            .title(problem.getTitle())
                            .slug(problem.getSlug())
                            .difficulty(problem.getDifficulty())
                            .leetcodeNumber(problem.getLeetcodeNumber())
                            .topicCode(topicCode)
                            .topicName(problem.getTopic().getName())
                            .description(problem.getDescription())
                            .inputFormat(problem.getInputFormat())
                            .outputFormat(problem.getOutputFormat())
                            .constraints(problem.getConstraints())
                            .sampleInput(problem.getSampleInput())
                            .sampleOutput(problem.getSampleOutput())
                            .sampleExplanation(problem.getSampleExplanation())
                            .progressStatus(progress.getStatus())
                            .attempts(progress.getAttempts())
                            .timeSpentSeconds(progress.getTimeSpentSeconds())
                            .solvedAt(progress.getSolvedAt())
                            .hints(hints)
                            .revision(revision)
                            .build();

            // 10. Add the problem to its pattern.
            pattern.getProblems().add(problemDetail);
            pattern.setProblemCount(pattern.getProblems().size());

            totalProblems++;
        }

        // 11. Remove patterns that have no attempted or solved problems.
        List<PatternPlaybookDto> populatedPatterns = patternMap.values()
                .stream()
                .filter(pattern -> !pattern.getProblems().isEmpty())
                .collect(Collectors.toList());

        // 12. Return the complete Playbook.
        return PlaybookResponse.builder()
                .patterns(populatedPatterns)
                .totalAttemptedOrSolvedProblems(totalProblems)
                .build();
    }

    private HintDto mapHint(Hint hint, int unlockedLevel) {

        // The Playbook intentionally includes content for all four hints.
        // The unlocked flag still reflects the student's actual progress.
        return HintDto.builder()
                .id(hint.getId())
                .level(hint.getLevel())
                .title(hint.getTitle())
                .content(hint.getContent())
                .timeComplexity(hint.getTimeComplexity())
                .spaceComplexity(hint.getSpaceComplexity())
                .unlocked(hint.getLevel() <= unlockedLevel)
                .build();
    }
}
