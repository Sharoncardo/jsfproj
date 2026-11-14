package com.dsaplatform.service.impl;

import com.dsaplatform.dto.revision.ProblemRevisionDto;
import com.dsaplatform.dto.revision.SaveRevisionRequest;
import com.dsaplatform.entity.Problem;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.UserProblemRevision;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.ProblemRepository;
import com.dsaplatform.repository.UserProblemRevisionRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.RevisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevisionServiceImpl implements RevisionService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final UserProblemRevisionRepository revisionRepository;

    @Override
    @Transactional(readOnly = true)
    public ProblemRevisionDto getRevision(UUID userId, Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", problemId));

        return revisionRepository.findByUserIdAndProblemId(userId, problemId)
                .map(this::toDto)
                .orElseGet(() -> ProblemRevisionDto.builder()
                        .problemId(problem.getId())
                        .problemTitle(problem.getTitle())
                        .problemSlug(problem.getSlug())
                        .build());
    }

    @Override
    @Transactional
    public ProblemRevisionDto saveRevision(UUID userId, Long problemId, SaveRevisionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", problemId));

        UserProblemRevision revision = revisionRepository.findByUserIdAndProblemId(userId, problemId)
                .orElseGet(() -> UserProblemRevision.builder()
                        .user(user)
                        .problem(problem)
                        .build());

        revision.setPersonalMnemonic(request.getPersonalMnemonic());
        revision.setPersonalApproach(request.getPersonalApproach());
        revision.setReasoning(request.getReasoning());
        revision.setTimeComplexity(request.getTimeComplexity());
        revision.setTimeComplexityExplanation(request.getTimeComplexityExplanation());
        revision.setSpaceComplexity(request.getSpaceComplexity());
        revision.setSpaceComplexityExplanation(request.getSpaceComplexityExplanation());

        UserProblemRevision saved = revisionRepository.save(revision);
        return toDto(saved);
    }

    private ProblemRevisionDto toDto(UserProblemRevision rev) {
        return ProblemRevisionDto.builder()
                .id(rev.getId())
                .problemId(rev.getProblem().getId())
                .problemTitle(rev.getProblem().getTitle())
                .problemSlug(rev.getProblem().getSlug())
                .personalMnemonic(rev.getPersonalMnemonic())
                .personalApproach(rev.getPersonalApproach())
                .reasoning(rev.getReasoning())
                .timeComplexity(rev.getTimeComplexity())
                .timeComplexityExplanation(rev.getTimeComplexityExplanation())
                .spaceComplexity(rev.getSpaceComplexity())
                .spaceComplexityExplanation(rev.getSpaceComplexityExplanation())
                .updatedAt(rev.getUpdatedAt())
                .build();
    }
}
