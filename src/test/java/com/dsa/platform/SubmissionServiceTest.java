package com.dsa.platform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import com.dsa.platform.problem.entity.Problem;
import com.dsa.platform.problem.repository.ProblemRepository;
import com.dsa.platform.submission.dto.SubmissionRequest;
import com.dsa.platform.submission.dto.SubmissionResponse;
import com.dsa.platform.submission.service.SubmissionService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SubmissionServiceTest {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ProblemRepository problemRepository;

    private Long testProblemId;
    private String testSessionId;

    @BeforeEach
    void setUp() {
        Problem problem = problemRepository.findBySlug("two-sum")
                .orElseThrow(() -> new IllegalStateException("Sample problem two-sum not found"));
        testProblemId = problem.getId();
        testSessionId = "test-session-" + UUID.randomUUID();
    }

    @Test
    void testSubmitSolutionAccepted() {
        SubmissionRequest request = new SubmissionRequest();
        request.setProblemId(testProblemId);
        request.setLanguage("java");
        request.setSessionId(testSessionId);
        request.setSourceCode("public class Solution { public int[] twoSum(int[] nums, int target) { return new int[]{0, 1}; } }");

        SubmissionResponse response = submissionService.submitSolution(request);

        assertNotNull(response);
        assertEquals("ACCEPTED", response.getStatus());
        assertTrue(response.getFeedback().contains("Success"));
        assertTrue(response.getExecutionTimeMs() > 0);
        assertTrue(response.getMemoryKb() > 0);
        assertEquals(testProblemId, response.getProblemId());
        assertEquals("Two Sum", response.getProblemTitle());
    }

    @Test
    void testSubmitSolutionCompilationErrorOnShortCode() {
        SubmissionRequest request = new SubmissionRequest();
        request.setProblemId(testProblemId);
        request.setLanguage("java");
        request.setSessionId(testSessionId);
        request.setSourceCode("short");

        SubmissionResponse response = submissionService.submitSolution(request);

        assertNotNull(response);
        assertEquals("COMPILATION_ERROR", response.getStatus());
        assertTrue(response.getFeedback().contains("Compilation Failed"));
    }

    @Test
    void testSubmitSolutionWrongAnswerOnMissingMethodStructure() {
        SubmissionRequest request = new SubmissionRequest();
        request.setProblemId(testProblemId);
        request.setLanguage("java");
        request.setSessionId(testSessionId);
        request.setSourceCode("int a = 1; int b = 2; int c = a + b; System.out.println(c);");

        SubmissionResponse response = submissionService.submitSolution(request);

        assertNotNull(response);
        assertEquals("WRONG_ANSWER", response.getStatus());
        assertTrue(response.getFeedback().contains("Evaluation Failed"));
    }

    @Test
    void testGetSubmissionsByProblemAndSession() {
        SubmissionRequest request = new SubmissionRequest();
        request.setProblemId(testProblemId);
        request.setLanguage("python");
        request.setSessionId(testSessionId);
        request.setSourceCode("def twoSum(nums, target):\n    return [0, 1]");

        submissionService.submitSolution(request);

        List<SubmissionResponse> submissions = submissionService.getSubmissionsByProblemAndSession(testProblemId, testSessionId);
        assertFalse(submissions.isEmpty());
        assertEquals("ACCEPTED", submissions.get(0).getStatus());
        assertEquals("python", submissions.get(0).getLanguage());
    }

    @Test
    void testGetSubmissionById() {
        SubmissionRequest request = new SubmissionRequest();
        request.setProblemId(testProblemId);
        request.setLanguage("cpp");
        request.setSessionId(testSessionId);
        request.setSourceCode("class Solution { public: vector<int> twoSum(vector<int>& n, int t) { return {0, 1}; } };");

        SubmissionResponse created = submissionService.submitSolution(request);
        SubmissionResponse fetched = submissionService.getSubmissionById(created.getId());

        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());
        assertEquals("cpp", fetched.getLanguage());
        assertEquals("ACCEPTED", fetched.getStatus());
    }
}
