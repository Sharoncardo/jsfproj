package com.dsa.platform.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.dsa.platform.topic.entity.Topic;
import com.dsa.platform.topic.repository.TopicRepository;
import com.dsa.platform.problem.entity.Problem;
import com.dsa.platform.problem.entity.LanguageOption;
import com.dsa.platform.problem.repository.ProblemRepository;
import com.dsa.platform.hint.entity.Hint;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TopicRepository topicRepository;
    private final ProblemRepository problemRepository;

    public DataInitializer(TopicRepository topicRepository, ProblemRepository problemRepository) {
        this.topicRepository = topicRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (problemRepository.count() > 0) {
            return;
        }

        // Initialize Topics
        Topic arraysTopic = topicRepository.save(new Topic("Arrays & Hashing", "arrays-and-hashing",
                "Array manipulation, hash map lookups, frequency counting, and sliding windows.", 1));
        Topic twoPointersTopic = topicRepository.save(new Topic("Two Pointers", "two-pointers",
                "Techniques using two indices moving towards each other or at varying speeds.", 2));
        Topic stacksTopic = topicRepository.save(new Topic("Stacks", "stacks",
                "Last-In-First-Out data structure for bracket matching, monotonic stacks, and parsing.", 3));
        Topic binarySearchTopic = topicRepository.save(new Topic("Binary Search", "binary-search",
                "Logarithmic search strategies on sorted collections and monotonic answer spaces.", 4));
        Topic dpTopic = topicRepository.save(new Topic("Dynamic Programming", "dynamic-programming",
                "Optimal substructure, overlapping subproblems, memoization, and bottom-up tabulation.", 5));
        Topic linkedListsTopic = topicRepository.save(new Topic("Linked Lists", "linked-lists",
                "Pointer manipulation, in-place node reversals, and fast/slow pointer algorithms.", 6));

        // 1. Two Sum
        createProblem(
                "Two Sum",
                "two-sum",
                "Easy",
                "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\n" +
                "You may assume that each input would have exactly one solution, and you may not use the same element twice.\n\n" +
                "You can return the answer in any order.",
                "nums = [2,7,11,15], target = 9",
                "[0,1]",
                "2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\n-10^9 <= target <= 10^9\nOnly one valid answer exists.",
                "nums = [2, 7, 11, 15], target = 9",
                "[0, 1]",
                "Because nums[0] + nums[1] == 2 + 7 == 9, we return [0, 1].",
                arraysTopic,
                new String[]{
                        "Consider what you need for each number x: you need to quickly determine whether (target - x) already exists in the collection.",
                        "Instead of scanning through the array using a nested loop (which takes O(n) per element), use a Hash Map (Hash Table) for constant-time O(1) lookups.",
                        "1. Create an empty hash map storing value-to-index pairs.\n2. Loop through nums with index i.\n3. Calculate complement = target - nums[i].\n4. If complement is in the map, return [map.get(complement), i].\n5. Otherwise, put nums[i] and i into the map.",
                        "One-pass Hash Map approach. Time Complexity: O(n) as we traverse the list containing n elements only once and map lookup is O(1). Space Complexity: O(n) for the hash map storing at most n elements."
                },
                new String[]{"O(n)", "O(n)"},
                "// C++ Solution\n#include <vector>\n#include <unordered_map>\nusing namespace std;\n\nclass Solution {\npublic:\n    vector<int> twoSum(vector<int>& nums, int target) {\n        unordered_map<int, int> map;\n        for (int i = 0; i < nums.size(); i++) {\n            int comp = target - nums[i];\n            if (map.count(comp)) return {map[comp], i};\n            map[nums[i]] = i;\n        }\n        return {};\n    }\n};",
                "// Java Solution\nimport java.util.HashMap;\nimport java.util.Map;\n\nclass Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            map.put(nums[i], i);\n        }\n        return new int[]{};\n    }\n}",
                "# Python Solution\nclass Solution:\n    def twoSum(self, nums: list[int], target: int) -> list[int]:\n        lookup = {}\n        for i, num in enumerate(nums):\n            comp = target - num\n            if comp in lookup:\n                return [lookup[comp], i]\n            lookup[num] = i\n        return []"
        );

        // 2. Reverse a String
        createProblem(
                "Reverse a String",
                "reverse-a-string",
                "Easy",
                "Write a function that reverses a string. The input string is given as an array of characters s.\n\n" +
                "You must do this by modifying the input array in-place with O(1) extra memory.",
                "s = [\"h\",\"e\",\"l\",\"l\",\"o\"]",
                "[\"o\",\"l\",\"l\",\"e\",\"h\"]",
                "1 <= s.length <= 10^5\ns[i] is a printable ASCII character.",
                "s = [\"h\",\"e\",\"l\",\"l\",\"o\"]",
                "[\"o\",\"l\",\"l\",\"e\",\"h\"]",
                "The first and last characters are swapped iteratively until the pointers meet in the middle.",
                twoPointersTopic,
                new String[]{
                        "Think about how elements at symmetric positions swap places: the 1st swaps with the last, the 2nd with the second-to-last.",
                        "Use the Two Pointers technique — place one pointer at the start (left) and another pointer at the end (right) of the character array.",
                        "1. Initialize left = 0 and right = s.length - 1.\n2. While left < right: swap s[left] and s[right].\n3. Increment left and decrement right.\n4. Stop when left >= right.",
                        "In-place Two Pointers. Time Complexity: O(n) where n is the length of the string, performing n/2 swaps. Space Complexity: O(1) auxiliary space as the array is mutated directly in-place."
                },
                new String[]{"O(n)", "O(1)"},
                "// C++ Solution\n#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    void reverseString(vector<char>& s) {\n        int left = 0, right = s.size() - 1;\n        while (left < right) {\n            swap(s[left++], s[right--]);\n        }\n    }\n};",
                "// Java Solution\nclass Solution {\n    public void reverseString(char[] s) {\n        int left = 0, right = s.length - 1;\n        while (left < right) {\n            char temp = s[left];\n            s[left] = s[right];\n            s[right] = temp;\n            left++;\n            right--;\n        }\n    }\n}",
                "# Python Solution\nclass Solution:\n    def reverseString(self, s: list[str]) -> None:\n        left, right = 0, len(s) - 1\n        while left < right:\n            s[left], s[right] = s[right], s[left]\n            left += 1\n            right -= 1"
        );

        // 3. Valid Parentheses
        createProblem(
                "Valid Parentheses",
                "valid-parentheses",
                "Easy",
                "Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\n" +
                "An input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.",
                "s = \"()[]{}\"",
                "true",
                "1 <= s.length <= 10^4\ns consists of parentheses only '()[]{}'.",
                "s = \"([{}])\"",
                "true",
                "Every opening bracket has an appropriate closing bracket correctly matched in inner-to-outer order.",
                stacksTopic,
                new String[]{
                        "Notice that the most recently opened bracket must be the very first one closed. What data structure models this LIFO behavior?",
                        "A Stack data structure naturally preserves LIFO (Last-In, First-Out) order to match nested delimiters.",
                        "1. Initialize an empty stack.\n2. Iterate through each character in s.\n3. If it is an opening bracket '(', '{', or '[', push it onto the stack.\n4. If it is a closing bracket, check if the stack is empty (invalid) or if the top doesn't match the closing bracket (invalid); otherwise pop the stack.\n5. At the end, the string is valid if and only if the stack is completely empty.",
                        "Stack Matching. Time Complexity: O(n) because we process each of the n characters exactly once. Space Complexity: O(n) in the worst case when all characters are opening brackets stored on the stack."
                },
                new String[]{"O(n)", "O(n)"},
                "// C++ Solution\n#include <string>\n#include <stack>\nusing namespace std;\n\nclass Solution {\npublic:\n    bool isValid(string s) {\n        stack<char> st;\n        for (char c : s) {\n            if (c == '(') st.push(')');\n            else if (c == '{') st.push('}');\n            else if (c == '[') st.push(']');\n            else {\n                if (st.empty() || st.top() != c) return false;\n                st.pop();\n            }\n        }\n        return st.empty();\n    }\n};",
                "// Java Solution\nimport java.util.ArrayDeque;\nimport java.util.Deque;\n\nclass Solution {\n    public boolean isValid(String s) {\n        Deque<Character> stack = new ArrayDeque<>();\n        for (char c : s.toCharArray()) {\n            if (c == '(') stack.push(')');\n            else if (c == '{') stack.push('}');\n            else if (c == '[') stack.push(']');\n            else if (stack.isEmpty() || stack.pop() != c) return false;\n        }\n        return stack.isEmpty();\n    }\n}",
                "# Python Solution\nclass Solution:\n    def isValid(self, s: str) -> bool:\n        stack = []\n        mapping = {')': '(', '}': '{', ']': '['}\n        for char in s:\n            if char in mapping:\n                top = stack.pop() if stack else '#'\n                if mapping[char] != top:\n                    return False\n            else:\n                stack.append(char)\n        return not stack"
        );

        // 4. Binary Search
        createProblem(
                "Binary Search",
                "binary-search",
                "Easy",
                "Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums.\n\n" +
                "If target exists, then return its index. Otherwise, return -1.\n\n" +
                "You must write an algorithm with O(log n) runtime complexity.",
                "nums = [-1,0,3,5,9,12], target = 9",
                "4",
                "1 <= nums.length <= 10^4\n-10^4 < nums[i], target < 10^4\nAll integers in nums are unique.\nnums is sorted in ascending order.",
                "nums = [-1,0,3,5,9,12], target = 9",
                "4",
                "9 exists in nums and its index is 4.",
                binarySearchTopic,
                new String[]{
                        "Because the array is sorted, comparing the target with the middle element tells you which entire half can be safely ignored.",
                        "Binary Search (Divide and Conquer on monotonic data). Maintain two boundaries: left and right.",
                        "1. Set left = 0, right = nums.length - 1.\n2. While left <= right: calculate mid = left + (right - left) / 2 to avoid overflow.\n3. If nums[mid] == target, return mid.\n4. If nums[mid] < target, discard left half: left = mid + 1.\n5. If nums[mid] > target, discard right half: right = mid - 1.\n6. If loop ends without match, return -1.",
                        "Optimal Binary Search. Time Complexity: O(log n) because the search interval is halved at each iteration. Space Complexity: O(1) iterative space."
                },
                new String[]{"O(log n)", "O(1)"},
                "// C++ Solution\n#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int search(vector<int>& nums, int target) {\n        int left = 0, right = nums.size() - 1;\n        while (left <= right) {\n            int mid = left + (right - left) / 2;\n            if (nums[mid] == target) return mid;\n            else if (nums[mid] < target) left = mid + 1;\n            else right = mid - 1;\n        }\n        return -1;\n    }\n};",
                "// Java Solution\nclass Solution {\n    public int search(int[] nums, int target) {\n        int left = 0, right = nums.length - 1;\n        while (left <= right) {\n            int mid = left + (right - left) / 2;\n            if (nums[mid] == target) return mid;\n            else if (nums[mid] < target) left = mid + 1;\n            else right = mid - 1;\n        }\n        return -1;\n    }\n}",
                "# Python Solution\nclass Solution:\n    def search(self, nums: list[int], target: int) -> int:\n        left, right = 0, len(nums) - 1\n        while left <= right:\n            mid = left + (right - left) // 2\n            if nums[mid] == target:\n                return mid\n            elif nums[mid] < target:\n                left = mid + 1\n            else:\n                right = mid - 1\n        return -1"
        );

        // 5. Maximum Subarray
        createProblem(
                "Maximum Subarray",
                "maximum-subarray",
                "Medium",
                "Given an integer array nums, find the subarray with the largest sum, and return its sum.\n\n" +
                "A subarray is a contiguous non-empty sequence of elements within an array.",
                "nums = [-2,1,-3,4,-1,2,1,-5,4]",
                "6",
                "1 <= nums.length <= 10^5\n-10^4 <= nums[i] <= 10^4",
                "nums = [-2,1,-3,4,-1,2,1,-5,4]",
                "6",
                "The contiguous subarray [4,-1,2,1] has the largest sum = 6.",
                dpTopic,
                new String[]{
                        "At any number, does a negative running prefix sum help future elements? If your running sum becomes negative, starting fresh at the next number is strictly better.",
                        "Dynamic Programming / Kadane's Algorithm. The subproblem is finding the maximum subarray sum ending at each index i.",
                        "1. Initialize currentSum = nums[0] and maxSum = nums[0].\n2. Iterate from index 1 to n - 1.\n3. At index i, decide whether to append nums[i] to currentSum or start new subarray: currentSum = max(nums[i], currentSum + nums[i]).\n4. Update maxSum = max(maxSum, currentSum).\n5. Return maxSum.",
                        "Kadane's Dynamic Programming. Time Complexity: O(n) single pass through the array. Space Complexity: O(1) as only two scalar accumulators are maintained."
                },
                new String[]{"O(n)", "O(1)"},
                "// C++ Solution\n#include <vector>\n#include <algorithm>\nusing namespace std;\n\nclass Solution {\npublic:\n    int maxSubArray(vector<int>& nums) {\n        int curr = nums[0], maxVal = nums[0];\n        for (size_t i = 1; i < nums.size(); i++) {\n            curr = max(nums[i], curr + nums[i]);\n            maxVal = max(maxVal, curr);\n        }\n        return maxVal;\n    }\n};",
                "// Java Solution\nclass Solution {\n    public int maxSubArray(int[] nums) {\n        int currentSum = nums[0];\n        int maxSum = nums[0];\n        for (int i = 1; i < nums.length; i++) {\n            currentSum = Math.max(nums[i], currentSum + nums[i]);\n            maxSum = Math.max(maxSum, currentSum);\n        }\n        return maxSum;\n    }\n}",
                "# Python Solution\nclass Solution:\n    def maxSubArray(self, nums: list[int]) -> int:\n        curr_sum = max_sum = nums[0]\n        for x in nums[1:]:\n            curr_sum = max(x, curr_sum + x)\n            max_sum = max(max_sum, curr_sum)\n        return max_sum"
        );

        // 6. Fibonacci
        createProblem(
                "Fibonacci",
                "fibonacci",
                "Easy",
                "The Fibonacci numbers, commonly denoted F(n), form a sequence where each number is the sum of the two preceding ones, starting from 0 and 1.\n\n" +
                "That is:\nF(0) = 0, F(1) = 1\nF(n) = F(n - 1) + F(n - 2), for n > 1.\n\nGiven n, calculate F(n).",
                "n = 4",
                "3",
                "0 <= n <= 30",
                "n = 4",
                "3",
                "F(4) = F(3) + F(2) = 2 + 1 = 3.",
                dpTopic,
                new String[]{
                        "Direct recursive calculation F(n-1) + F(n-2) recalculates identical values (e.g., F(2)) repeatedly, leading to exponential O(2^n) work.",
                        "Overlapping Subproblems and Optimal Substructure indicate Dynamic Programming (memoization or bottom-up tabulation).",
                        "1. If n <= 1, return n directly.\n2. Store base values prev2 = 0 and prev1 = 1.\n3. Loop from i = 2 to n: compute current = prev1 + prev2, then shift prev2 = prev1 and prev1 = current.\n4. Return prev1.",
                        "Iterative Space-Optimized DP. Time Complexity: O(n) linear loop from 2 to n. Space Complexity: O(1) auxiliary space, replacing full array or recursion call stack."
                },
                new String[]{"O(n)", "O(1)"},
                "// C++ Solution\nclass Solution {\npublic:\n    int fib(int n) {\n        if (n <= 1) return n;\n        int prev2 = 0, prev1 = 1;\n        for (int i = 2; i <= n; i++) {\n            int curr = prev1 + prev2;\n            prev2 = prev1;\n            prev1 = curr;\n        }\n        return prev1;\n    }\n};",
                "// Java Solution\nclass Solution {\n    public int fib(int n) {\n        if (n <= 1) return n;\n        int prev2 = 0, prev1 = 1;\n        for (int i = 2; i <= n; i++) {\n            int current = prev1 + prev2;\n            prev2 = prev1;\n            prev1 = current;\n        }\n        return prev1;\n    }\n}",
                "# Python Solution\nclass Solution:\n    def fib(self, n: int) -> int:\n        if n <= 1:\n            return n\n        prev2, prev1 = 0, 1\n        for _ in range(2, n + 1):\n            prev2, prev1 = prev1, prev2 + prev1\n        return prev1"
        );

        // 7. Reverse Linked List
        createProblem(
                "Reverse Linked List",
                "reverse-linked-list",
                "Medium",
                "Given the head of a singly linked list, reverse the list, and return the reversed list.",
                "head = [1,2,3,4,5]",
                "[5,4,3,2,1]",
                "The number of nodes in the list is the range [0, 5000].\n-5000 <= Node.val <= 5000",
                "head = [1, 2, 3, 4, 5]",
                "[5, 4, 3, 2, 1]",
                "Every node's next pointer is redirected to point to its preceding node.",
                linkedListsTopic,
                new String[]{
                        "As you visit node curr, you must point curr.next to the node visited just before it. But if you overwrite curr.next, you lose the rest of the list!",
                        "Pointer Manipulation with Three Pointers: prev, curr, and a temporary nextNode pointer to prevent reference loss.",
                        "1. Initialize prev = null, curr = head.\n2. While curr is not null: save nextNode = curr.next; redirect curr.next = prev; advance prev = curr; advance curr = nextNode.\n3. When curr becomes null, prev points to the new head of the reversed list. Return prev.",
                        "Iterative Three-Pointer Technique. Time Complexity: O(n) traversing each of the n nodes exactly once. Space Complexity: O(1) in-place pointer adjustment without heap or recursion stack allocation."
                },
                new String[]{"O(n)", "O(1)"},
                "// C++ Solution\nstruct ListNode {\n    int val;\n    ListNode *next;\n    ListNode(int x) : val(x), next(nullptr) {}\n};\n\nclass Solution {\npublic:\n    ListNode* reverseList(ListNode* head) {\n        ListNode* prev = nullptr;\n        ListNode* curr = head;\n        while (curr) {\n            ListNode* nextNode = curr->next;\n            curr->next = prev;\n            prev = curr;\n            curr = nextNode;\n        }\n        return prev;\n    }\n};",
                "// Java Solution\nclass ListNode {\n    int val;\n    ListNode next;\n    ListNode(int val) { this.val = val; }\n}\n\nclass Solution {\n    public ListNode reverseList(ListNode head) {\n        ListNode prev = null;\n        ListNode curr = head;\n        while (curr != null) {\n            ListNode nextNode = curr.next;\n            curr.next = prev;\n            prev = curr;\n            curr = nextNode;\n        }\n        return prev;\n    }\n}",
                "# Python Solution\nclass ListNode:\n    def __init__(self, val=0, next=None):\n        self.val = val\n        self.next = next\n\nclass Solution:\n    def reverseList(self, head: ListNode) -> ListNode:\n        prev = None\n        curr = head\n        while curr:\n            next_node = curr.next\n            curr.next = prev\n            prev = curr\n            curr = next_node\n        return prev"
        );
    }

    private void createProblem(
            String title,
            String slug,
            String difficulty,
            String description,
            String inputFormat,
            String outputFormat,
            String constraints,
            String sampleInput,
            String sampleOutput,
            String sampleExplanation,
            Topic topic,
            String[] hintsContent,
            String[] complexities,
            String cppTemplate,
            String javaTemplate,
            String pythonTemplate
    ) {
        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setSlug(slug);
        problem.setDifficulty(difficulty);
        problem.setDescription(description);
        problem.setInputFormat(inputFormat);
        problem.setOutputFormat(outputFormat);
        problem.setConstraints(constraints);
        problem.setSampleInput(sampleInput);
        problem.setSampleOutput(sampleOutput);
        problem.setSampleExplanation(sampleExplanation);
        problem.setTopic(topic);

        // Add 4 progressive hints
        String[] titles = {
                "Level 1: Conceptual Nudge",
                "Level 2: Relevant DSA Concept",
                "Level 3: Step-by-Step Algorithmic Logic",
                "Level 4: Optimized Approach & Complexity Analysis"
        };

        for (int i = 0; i < 4; i++) {
            Hint hint = new Hint();
            hint.setLevel(i + 1);
            hint.setTitle(titles[i]);
            hint.setContent(hintsContent[i]);
            if (i == 3) {
                hint.setTimeComplexity(complexities[0]);
                hint.setSpaceComplexity(complexities[1]);
            }
            problem.addHint(hint);
        }

        // Add language options (C++, Java, Python)
        problem.addLanguageOption(new LanguageOption("C++", "cpp", cppTemplate));
        problem.addLanguageOption(new LanguageOption("Java", "java", javaTemplate));
        problem.addLanguageOption(new LanguageOption("Python", "python", pythonTemplate));

        problemRepository.save(problem);
    }
}
