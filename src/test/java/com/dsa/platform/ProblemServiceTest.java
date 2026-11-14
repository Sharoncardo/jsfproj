package com.dsa.platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.dsa.platform.problem.service.ProblemService;
import com.dsa.platform.problem.dto.ProblemSummaryDTO;
import com.dsa.platform.problem.dto.ProblemDetailDTO;
import com.dsa.platform.problem.dto.LanguageOptionDTO;

@SpringBootTest
public class ProblemServiceTest {

    @Autowired
    private ProblemService problemService;

    @Test
    void testSampleProblemsLoaded() {
        List<ProblemSummaryDTO> problems = problemService.getProblems(null, null, null);
        assertTrue(problems.size() >= 7, "Should load at least 7 sample problems");

        boolean hasTwoSum = problems.stream().anyMatch(p -> p.getTitle().equals("Two Sum"));
        boolean hasReverseString = problems.stream().anyMatch(p -> p.getTitle().equals("Reverse a String"));
        boolean hasValidParentheses = problems.stream().anyMatch(p -> p.getTitle().equals("Valid Parentheses"));
        boolean hasBinarySearch = problems.stream().anyMatch(p -> p.getTitle().equals("Binary Search"));
        boolean hasMaxSubarray = problems.stream().anyMatch(p -> p.getTitle().equals("Maximum Subarray"));
        boolean hasFibonacci = problems.stream().anyMatch(p -> p.getTitle().equals("Fibonacci"));
        boolean hasReverseLinkedList = problems.stream().anyMatch(p -> p.getTitle().equals("Reverse Linked List"));

        assertTrue(hasTwoSum);
        assertTrue(hasReverseString);
        assertTrue(hasValidParentheses);
        assertTrue(hasBinarySearch);
        assertTrue(hasMaxSubarray);
        assertTrue(hasFibonacci);
        assertTrue(hasReverseLinkedList);
    }

    @Test
    void testFilterByDifficulty() {
        List<ProblemSummaryDTO> easyProblems = problemService.getProblems(null, null, "Easy");
        assertFalse(easyProblems.isEmpty());
        for (ProblemSummaryDTO p : easyProblems) {
            assertEquals("Easy", p.getDifficulty());
        }

        List<ProblemSummaryDTO> mediumProblems = problemService.getProblems(null, null, "Medium");
        assertFalse(mediumProblems.isEmpty());
        for (ProblemSummaryDTO p : mediumProblems) {
            assertEquals("Medium", p.getDifficulty());
        }
    }

    @Test
    void testSearchByTitle() {
        List<ProblemSummaryDTO> results = problemService.getProblems("binary", null, null);
        assertEquals(1, results.size());
        assertEquals("Binary Search", results.get(0).getTitle());
    }

    @Test
    void testFilterByTopic() {
        List<ProblemSummaryDTO> results = problemService.getProblems(null, "dynamic-programming", null);
        assertEquals(2, results.size()); // Maximum Subarray and Fibonacci
    }

    @Test
    void testProblemDetailWithLanguageOptions() {
        ProblemDetailDTO detail = problemService.getProblemDetailBySlug("two-sum");
        assertNotNull(detail);
        assertEquals("Two Sum", detail.getTitle());
        assertEquals("Easy", detail.getDifficulty());
        assertNotNull(detail.getConstraints());
        assertNotNull(detail.getSampleInput());
        assertNotNull(detail.getSampleOutput());

        List<LanguageOptionDTO> languages = detail.getLanguageOptions();
        assertEquals(3, languages.size(), "Must support C++, Java, and Python");

        boolean hasCpp = languages.stream().anyMatch(l -> l.getLanguageKey().equals("cpp"));
        boolean hasJava = languages.stream().anyMatch(l -> l.getLanguageKey().equals("java"));
        boolean hasPython = languages.stream().anyMatch(l -> l.getLanguageKey().equals("python"));

        assertTrue(hasCpp);
        assertTrue(hasJava);
        assertTrue(hasPython);
    }
}
