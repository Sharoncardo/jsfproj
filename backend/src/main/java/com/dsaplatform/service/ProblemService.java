package com.dsaplatform.service;

import com.dsaplatform.dto.problem.ProblemDetailDto;
import com.dsaplatform.dto.problem.ProblemSummaryDto;
import com.dsaplatform.dto.problem.TopicDto;

import java.util.List;

public interface ProblemService {

    List<TopicDto> getAllTopics();

    List<ProblemSummaryDto> getProblems(String search, String topic, String difficulty);

    ProblemDetailDto getProblemById(Long id);

    ProblemDetailDto getProblemBySlug(String slug);
}
