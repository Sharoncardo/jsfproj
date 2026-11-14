package com.dsa.platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.dsa.platform.hint.controller.HintController;
import com.dsa.platform.hint.repository.HintRepository;
import com.dsa.platform.hint.service.HintService;
import com.dsa.platform.problem.controller.ProblemController;
import com.dsa.platform.problem.repository.LanguageOptionRepository;
import com.dsa.platform.problem.repository.ProblemRepository;
import com.dsa.platform.problem.service.ProblemService;
import com.dsa.platform.submission.controller.SubmissionController;
import com.dsa.platform.submission.repository.SubmissionRepository;
import com.dsa.platform.submission.service.SubmissionService;
import com.dsa.platform.topic.controller.TopicController;
import com.dsa.platform.topic.repository.TopicRepository;
import com.dsa.platform.topic.service.TopicService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DsaPlatformApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private HintService hintService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private HintRepository hintRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LanguageOptionRepository languageOptionRepository;

    @Autowired
    private ProblemController problemController;

    @Autowired
    private HintController hintController;

    @Autowired
    private SubmissionController submissionController;

    @Autowired
    private TopicController topicController;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "ApplicationContext should be successfully loaded");
        assertNotNull(problemService, "ProblemService bean must be injected");
        assertNotNull(hintService, "HintService bean must be injected");
        assertNotNull(submissionService, "SubmissionService bean must be injected");
        assertNotNull(topicService, "TopicService bean must be injected");
        assertNotNull(problemRepository, "ProblemRepository must be injected");
        assertNotNull(hintRepository, "HintRepository must be injected");
        assertNotNull(submissionRepository, "SubmissionRepository must be injected");
        assertNotNull(topicRepository, "TopicRepository must be injected");
        assertNotNull(languageOptionRepository, "LanguageOptionRepository must be injected");
        assertNotNull(problemController, "ProblemController must be injected");
        assertNotNull(hintController, "HintController must be injected");
        assertNotNull(submissionController, "SubmissionController must be injected");
        assertNotNull(topicController, "TopicController must be injected");
    }

    @Test
    void testSampleDataLoadedOnStartup() {
        assertTrue(problemRepository.count() >= 7, "Sample problems should be loaded on startup");
        assertTrue(topicRepository.count() >= 6, "Sample topics should be loaded on startup");
        assertTrue(hintRepository.count() > 0, "Sample hints should be loaded on startup");
        assertTrue(languageOptionRepository.count() > 0, "Language options should be loaded on startup");
    }
}
