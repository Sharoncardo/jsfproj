-- Flyway Migration V4: Seed Verified Problem Catalog, Progressive Hints & Boilerplate Templates

-- 1. Seed Topics (Aligned with Topic enum)
INSERT INTO topics (id, code, name, slug, description, display_order) VALUES
(1, 'ARRAYS', 'Arrays & Hashing', 'arrays', 'Array manipulation, frequency counting, prefix sums, and hash tables.', 1),
(2, 'STRINGS', 'Strings', 'strings', 'String parsing, two-pointer palindromes, and character mapping.', 2),
(3, 'LINKED_LIST', 'Linked Lists', 'linked-list', 'Pointers, reversals, fast & slow runner, and merge techniques.', 3),
(4, 'STACK_QUEUE', 'Stacks & Queues', 'stack-queue', 'LIFO parsing, monotonic stacks, and FIFO queue buffering.', 4),
(5, 'TREES', 'Trees & BST', 'trees', 'Binary trees, traversals, LCA, and BST validation.', 5),
(6, 'GRAPHS', 'Graphs', 'graphs', 'BFS, DFS, connected components, and topological sort.', 6),
(7, 'DYNAMIC_PROGRAMMING', 'Dynamic Programming', 'dynamic-programming', 'Subproblems, recurrence relations, memoization, and tabulation.', 7),
(8, 'SORTING_SEARCHING', 'Sorting & Searching', 'sorting-searching', 'Binary search on values and arrays, quicksort, and heapsort.', 8),
(9, 'GREEDY', 'Greedy Algorithms', 'greedy', 'Locally optimal choices and interval scheduling.', 9),
(10, 'BACKTRACKING', 'Backtracking', 'backtracking', 'Combinatorial exploration, permutations, and constraint satisfaction.', 10);

-- 2. Seed Verified Problems (Matching existing seed slugs + LeetCode standards)

-- Problem 1: Two Sum (LC 1 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(1, 1, 'ARRAYS', 'Two Sum', 'two-sum', 'EASY', 1,
'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.',
'nums = [2,7,11,15], target = 9', '[0,1]',
'2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\n-10^9 <= target <= 10^9\nOnly one valid answer exists.',
'nums = [2,7,11,15], target = 9', '[0,1]',
'Because nums[0] + nums[1] == 2 + 7 == 9, we return [0, 1].');

-- Problem 2: Best Time to Buy and Sell Stock (LC 121 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(2, 1, 'ARRAYS', 'Best Time to Buy and Sell Stock', 'best-time-to-buy-and-sell-stock', 'EASY', 121,
'You are given an array prices where prices[i] is the price of a given stock on the ith day. You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock. Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.',
'prices = [7,1,5,3,6,4]', '5',
'1 <= prices.length <= 10^5\n0 <= prices[i] <= 10^4',
'prices = [7,1,5,3,6,4]', '5',
'Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.');

-- Problem 3: Container With Most Water (LC 11 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(3, 1, 'ARRAYS', 'Container With Most Water', 'container-with-most-water', 'MEDIUM', 11,
'You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]). Find two lines that together with the x-axis form a container, such that the container contains the most water. Return the maximum amount of water a container can store.',
'height = [1,8,6,2,5,4,8,3,7]', '49',
'n == height.length\n2 <= n <= 10^5\n0 <= height[i] <= 10^4',
'height = [1,8,6,2,5,4,8,3,7]', '49',
'The vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water the container can contain is 49.');

-- Problem 4: Trapping Rain Water (LC 42 - Hard) [VERIFIED HARD]
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(4, 1, 'ARRAYS', 'Trapping Rain Water', 'trapping-rain-water', 'HARD', 42,
'Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.',
'height = [0,1,0,2,1,0,1,3,2,1,2,1]', '6',
'n == height.length\n1 <= n <= 2 * 10^4\n0 <= height[i] <= 10^5',
'height = [0,1,0,2,1,0,1,3,2,1,2,1]', '6',
'The elevation map [0,1,0,2,1,0,1,3,2,1,2,1] traps 6 units of rain water.');

-- Problem 5: Valid Palindrome (LC 125 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(5, 2, 'STRINGS', 'Valid Palindrome', 'valid-palindrome', 'EASY', 125,
'A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers. Given a string s, return true if it is a palindrome, or false otherwise.',
's = "A man, a plan, a canal: Panama"', 'true',
'1 <= s.length <= 2 * 10^5\ns consists only of printable ASCII characters.',
's = "A man, a plan, a canal: Panama"', 'true',
'"amanaplanacanalpanama" is a palindrome.');

-- Problem 6: Reverse a String (LC 344 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(6, 2, 'STRINGS', 'Reverse a String', 'reverse-a-string', 'EASY', 344,
'Write a function that reverses a string. The input string is given as an array of characters s. You must do this by modifying the input array in-place with O(1) extra memory.',
's = ["h","e","l","l","o"]', '["o","l","l","e","h"]',
'1 <= s.length <= 10^5\ns[i] is a printable ASCII character.',
's = ["h","e","l","l","o"]', '["o","l","l","e","h"]',
'Characters swapped from outer boundaries inward until pointers meet.');

-- Problem 7: Longest Substring Without Repeating Characters (LC 3 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(7, 2, 'STRINGS', 'Longest Substring Without Repeating Characters', 'longest-substring-without-repeating-characters', 'MEDIUM', 3,
'Given a string s, find the length of the longest substring without repeating characters.',
's = "abcabcbb"', '3',
'0 <= s.length <= 5 * 10^4\ns consists of English letters, digits, symbols and spaces.',
's = "abcabcbb"', '3',
'The answer is "abc", with the length of 3.');

-- Problem 8: Reverse Linked List (LC 206 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(8, 3, 'LINKED_LIST', 'Reverse Linked List', 'reverse-linked-list', 'EASY', 206,
'Given the head of a singly linked list, reverse the list, and return the reversed list.',
'head = [1,2,3,4,5]', '[5,4,3,2,1]',
'The number of nodes in the list is the range [0, 5000].\n-5000 <= Node.val <= 5000',
'head = [1,2,3,4,5]', '[5,4,3,2,1]',
'All link directions reversed in-place.');

-- Problem 9: Merge k Sorted Lists (LC 23 - Hard) [VERIFIED HARD]
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(9, 3, 'LINKED_LIST', 'Merge k Sorted Lists', 'merge-k-sorted-lists', 'HARD', 23,
'You are given an array of k linked-lists lists, each linked-list is sorted in ascending order. Merge all the linked-lists into one sorted linked-list and return it.',
'lists = [[1,4,5],[1,3,4],[2,6]]', '[1,1,2,3,4,4,5,6]',
'k == lists.length\n0 <= k <= 10^4\n0 <= lists[i].length <= 500\n-10^4 <= lists[i][j] <= 10^4\nlists[i] is sorted in ascending order.',
'lists = [[1,4,5],[1,3,4],[2,6]]', '[1,1,2,3,4,4,5,6]',
'All lists combined into one sorted sequence.');

-- Problem 10: Valid Parentheses (LC 20 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(10, 4, 'STACK_QUEUE', 'Valid Parentheses', 'valid-parentheses', 'EASY', 20,
'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid. Open brackets must be closed by the same type of brackets and in the correct order.',
's = "()[]{}"', 'true',
'1 <= s.length <= 10^4\ns consists of parentheses only ''()[]{}''.',
's = "()[]{}"', 'true',
'All delimiters correctly paired and nested.');

-- Problem 11: Daily Temperatures (LC 739 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(11, 4, 'STACK_QUEUE', 'Daily Temperatures', 'daily-temperatures', 'MEDIUM', 739,
'Given an array of integers temperatures represents the daily temperatures, return an array answer such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature. If there is no future day for which this is possible, keep answer[i] == 0 instead.',
'temperatures = [73,74,75,71,69,72,76,73]', '[1,1,4,2,1,1,0,0]',
'1 <= temperatures.length <= 10^5\n30 <= temperatures[i] <= 100',
'temperatures = [73,74,75,71,69,72,76,73]', '[1,1,4,2,1,1,0,0]',
'For day 0 (73), next warmer is day 1 (74), wait = 1 day.');

-- Problem 12: Binary Search (LC 704 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(12, 8, 'SORTING_SEARCHING', 'Binary Search', 'binary-search', 'EASY', 704,
'Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums. If target exists, then return its index. Otherwise, return -1. You must write an algorithm with O(log n) runtime complexity.',
'nums = [-1,0,3,5,9,12], target = 9', '4',
'1 <= nums.length <= 10^4\n-10^4 < nums[i], target < 10^4\nAll the integers in nums are unique.\nnums is sorted in ascending order.',
'nums = [-1,0,3,5,9,12], target = 9', '4',
'9 exists in nums and its index is 4.');

-- Problem 13: Maximum Depth of Binary Tree (LC 104 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(13, 5, 'TREES', 'Maximum Depth of Binary Tree', 'maximum-depth-of-binary-tree', 'EASY', 104,
'Given the root of a binary tree, return its maximum depth. A binary tree''s maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.',
'root = [3,9,20,null,null,15,7]', '3',
'The number of nodes in the tree is in the range [0, 10^4].\n-100 <= Node.val <= 100',
'root = [3,9,20,null,null,15,7]', '3',
'Longest path from root 3 to leaves 15 or 7 has length 3.');

-- Problem 14: Lowest Common Ancestor of a Binary Tree (LC 236 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(14, 5, 'TREES', 'Lowest Common Ancestor of a Binary Tree', 'lowest-common-ancestor', 'MEDIUM', 236,
'Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree. The lowest common ancestor is defined between two nodes p and q as the lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).',
'root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1', '3',
'The number of nodes in the tree is in the range [2, 10^5].\n-10^9 <= Node.val <= 10^9\nAll Node.val are unique.\np != q and both p and q will exist in the tree.',
'root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1', '3',
'The LCA of nodes 5 and 1 is 3.');

-- Problem 15: Number of Islands (LC 200 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(15, 6, 'GRAPHS', 'Number of Islands', 'number-of-islands', 'MEDIUM', 200,
'Given an m x n 2D binary grid grid which represents a map of ''1''s (land) and ''0''s (water), return the number of islands. An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.',
'grid = [["1","1","0","0","0"],["1","1","0","0","0"],["0","0","1","0","0"],["0","0","0","1","1"]]', '3',
'm == grid.length\nn == grid[i].length\n1 <= m, n <= 300\ngrid[i][j] is ''0'' or ''1''.',
'grid = [["1","1","0","0","0"],["1","1","0","0","0"],["0","0","1","0","0"],["0","0","0","1","1"]]', '3',
'There are 3 distinct connected land masses.');

-- Problem 16: Climbing Stairs (LC 70 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(16, 7, 'DYNAMIC_PROGRAMMING', 'Climbing Stairs', 'climbing-stairs', 'EASY', 70,
'You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?',
'n = 3', '3',
'1 <= n <= 45',
'n = 3', '3',
'Three ways to climb to the top: 1. 1 step + 1 step + 1 step, 2. 1 step + 2 steps, 3. 2 steps + 1 step.');

-- Problem 17: Maximum Subarray (LC 53 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(17, 7, 'DYNAMIC_PROGRAMMING', 'Maximum Subarray', 'maximum-subarray', 'MEDIUM', 53,
'Given an integer array nums, find the subarray with the largest sum, and return its sum. A subarray is a contiguous non-empty sequence of elements within an array.',
'nums = [-2,1,-3,4,-1,2,1,-5,4]', '6',
'1 <= nums.length <= 10^5\n-10^4 <= nums[i] <= 10^4',
'nums = [-2,1,-3,4,-1,2,1,-5,4]', '6',
'The contiguous subarray [4,-1,2,1] has the largest sum = 6.');

-- Problem 18: Coin Change (LC 322 - Medium)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(18, 7, 'DYNAMIC_PROGRAMMING', 'Coin Change', 'coin-change', 'MEDIUM', 322,
'You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money. Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.',
'coins = [1,2,5], amount = 11', '3',
'1 <= coins.length <= 12\n1 <= coins[i] <= 2^31 - 1\n0 <= amount <= 10^4',
'coins = [1,2,5], amount = 11', '3',
'11 = 5 + 5 + 1 (3 coins total).');

-- Problem 19: Edit Distance (LC 72 - Hard) [VERIFIED HARD]
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(19, 7, 'DYNAMIC_PROGRAMMING', 'Edit Distance', 'edit-distance', 'HARD', 72,
'Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2. You have the following three operations permitted on a word: Insert a character, Delete a character, Replace a character.',
'word1 = "horse", word2 = "ros"', '3',
'0 <= word1.length, word2.length <= 500\nword1 and word2 consist of lowercase English letters.',
'word1 = "horse", word2 = "ros"', '3',
'horse -> rorse (replace ''h'' with ''r''), rorse -> rose (remove ''r''), rose -> ros (remove ''e''). Total 3 operations.');

-- Problem 20: Fibonacci Number (LC 509 - Easy)
INSERT INTO problems (id, topic_id, topic_code, title, slug, difficulty, leetcode_number, description, input_format, output_format, constraints, sample_input, sample_output, sample_explanation) VALUES
(20, 7, 'DYNAMIC_PROGRAMMING', 'Fibonacci Number', 'fibonacci', 'EASY', 509,
'The Fibonacci numbers, commonly denoted F(n), form a sequence where each number is the sum of the two preceding ones, starting from 0 and 1. That is: F(0) = 0, F(1) = 1, F(n) = F(n - 1) + F(n - 2) for n > 1. Given n, calculate F(n).',
'n = 4', '3',
'0 <= n <= 30',
'n = 4', '3',
'F(4) = F(3) + F(2) = 2 + 1 = 3.');

-- 3. Seed Exactly 4 Concise Progressive Hints per Problem (No solutions leaked)

-- Hints for Problem 1: Two Sum
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(1, 1, 'Level 1: Conceptual Nudge', 'For each element x, you need to check whether (target - x) exists in the remainder of the array.', NULL, NULL),
(1, 2, 'Level 2: Relevant DSA Concept', 'Use a Hash Map to store numbers and their indices for constant-time lookup instead of nested loops.', NULL, NULL),
(1, 3, 'Level 3: Step-by-Step Logic', 'Iterate through the array. For each num, compute complement = target - num. If complement is in map, return indices; otherwise insert num and its index into the map.', NULL, NULL),
(1, 4, 'Level 4: Optimal Space-Time Analysis', 'Single pass hash table yields O(N) time and O(N) auxiliary space. Lookups and insertions run in O(1) average time.', 'O(N)', 'O(N)');

-- Hints for Problem 2: Best Time to Buy and Sell Stock
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(2, 1, 'Level 1: Conceptual Nudge', 'You want to buy at the lowest point seen so far and sell at the current price if profit is higher.', NULL, NULL),
(2, 2, 'Level 2: Relevant DSA Concept', 'Track a running minimum price and a running maximum profit in a single linear scan.', NULL, NULL),
(2, 3, 'Level 3: Step-by-Step Logic', 'Initialize minPrice to infinity, maxProfit to 0. Loop through prices: update minPrice = min(minPrice, price), then maxProfit = max(maxProfit, price - minPrice).', NULL, NULL),
(2, 4, 'Level 4: Optimal Space-Time Analysis', 'Single pass requires O(N) time and O(1) auxiliary space, tracking only two scalar variables.', 'O(N)', 'O(1)');

-- Hints for Problem 3: Container With Most Water
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(3, 1, 'Level 1: Conceptual Nudge', 'Area is bounded by the shorter of the two lines and the distance between them.', NULL, NULL),
(3, 2, 'Level 2: Relevant DSA Concept', 'Two Pointers converging from both ends. The wider the base, the better; moving the taller pointer can never increase area.', NULL, NULL),
(3, 3, 'Level 3: Step-by-Step Logic', 'Start with left = 0, right = n - 1. Calculate area = min(height[left], height[right]) * (right - left). Move whichever pointer has the smaller height inward.', NULL, NULL),
(3, 4, 'Level 4: Optimal Space-Time Analysis', 'Two-pointer convergence terminates in O(N) time and uses O(1) auxiliary memory.', 'O(N)', 'O(1)');

-- Hints for Problem 4: Trapping Rain Water (VERIFIED HARD)
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(4, 1, 'Level 1: Conceptual Nudge', 'Water retained above position i equals max(0, min(maxLeft, maxRight) - height[i]). The shorter boundary governs capacity.', NULL, NULL),
(4, 2, 'Level 2: Relevant DSA Concept', 'Two Pointers moving inward. Maintaining running leftMax and rightMax avoids allocating separate prefix/suffix arrays.', NULL, NULL),
(4, 3, 'Level 3: Step-by-Step Logic', 'Initialize left = 0, right = n - 1, leftMax = 0, rightMax = 0. While left < right: if height[left] < height[right], update leftMax and add leftMax - height[left] to total, increment left; else update rightMax and add rightMax - height[right] to total, decrement right.', NULL, NULL),
(4, 4, 'Level 4: Optimal Space-Time Analysis', 'Single pass two-pointer algorithm achieves optimal O(N) time and O(1) extra space, eliminating the O(N) memory requirement of prefix arrays.', 'O(N)', 'O(1)');

-- Hints for Problem 5: Valid Palindrome
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(5, 1, 'Level 1: Conceptual Nudge', 'Ignore non-alphanumeric characters and treat uppercase and lowercase identically.', NULL, NULL),
(5, 2, 'Level 2: Relevant DSA Concept', 'Two Pointers moving from left and right towards the center, skipping invalid characters.', NULL, NULL),
(5, 3, 'Level 3: Step-by-Step Logic', 'While left < right: skip non-alphanumeric chars for both left and right. If characters (in lowercase) mismatch, return false; otherwise advance pointers.', NULL, NULL),
(5, 4, 'Level 4: Optimal Space-Time Analysis', 'In-place two-pointer scan runs in O(N) time and O(1) auxiliary space without creating a sanitized copy string.', 'O(N)', 'O(1)');

-- Hints for Problem 6: Reverse a String
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(6, 1, 'Level 1: Conceptual Nudge', 'The first element must swap with the last, second with second-to-last, meeting in the middle.', NULL, NULL),
(6, 2, 'Level 2: Relevant DSA Concept', 'Two Pointers placed at index 0 and index n - 1.', NULL, NULL),
(6, 3, 'Level 3: Step-by-Step Logic', 'Initialize left = 0, right = len - 1. While left < right, swap s[left] with s[right], then left++ and right--.', NULL, NULL),
(6, 4, 'Level 4: Optimal Space-Time Analysis', 'Performs N / 2 swaps for O(N) time and strictly O(1) auxiliary space.', 'O(N)', 'O(1)');

-- Hints for Problem 7: Longest Substring Without Repeating Characters
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(7, 1, 'Level 1: Conceptual Nudge', 'Expand a window of characters until a duplicate is encountered, then shrink the window from the left.', NULL, NULL),
(7, 2, 'Level 2: Relevant DSA Concept', 'Sliding Window with a Hash Map or integer array tracking the most recent index of each character.', NULL, NULL),
(7, 3, 'Level 3: Step-by-Step Logic', 'Iterate right pointer over string. If s[right] was seen inside current window, jump left pointer to lastIndex + 1. Update max length and record s[right] position.', NULL, NULL),
(7, 4, 'Level 4: Optimal Space-Time Analysis', 'Sliding window visits each character at most twice for O(N) time and O(min(N, M)) space where M is charset size.', 'O(N)', 'O(min(N, M))');

-- Hints for Problem 8: Reverse Linked List
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(8, 1, 'Level 1: Conceptual Nudge', 'To reverse a pointer curr.next, you must first save the remainder of the list so it is not lost.', NULL, NULL),
(8, 2, 'Level 2: Relevant DSA Concept', 'Iterative Three Pointers: prev, curr, and nextTemp.', NULL, NULL),
(8, 3, 'Level 3: Step-by-Step Logic', 'Set prev = null, curr = head. While curr != null: nextTemp = curr.next; curr.next = prev; prev = curr; curr = nextTemp. Return prev.', NULL, NULL),
(8, 4, 'Level 4: Optimal Space-Time Analysis', 'Single traversal visits each node once in O(N) time and O(1) extra space without recursion stack overhead.', 'O(N)', 'O(1)');

-- Hints for Problem 9: Merge k Sorted Lists (VERIFIED HARD)
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(9, 1, 'Level 1: Conceptual Nudge', 'At any moment, the next smallest element must be the head of one of the k sorted lists.', NULL, NULL),
(9, 2, 'Level 2: Relevant DSA Concept', 'Min-Heap (Priority Queue) of size k to extract minimum in O(log k) time.', NULL, NULL),
(9, 3, 'Level 3: Step-by-Step Logic', 'Add non-null head of each list to min-heap. Pop minimum node, link to result list, and if popped node has a next node, insert next node into heap.', NULL, NULL),
(9, 4, 'Level 4: Optimal Space-Time Analysis', 'Min-heap of size k processes all N total nodes in O(N log k) time and O(k) auxiliary memory.', 'O(N log k)', 'O(k)');

-- Hints for Problem 10: Valid Parentheses
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(10, 1, 'Level 1: Conceptual Nudge', 'The most recently opened delimiter must be the first one closed.', NULL, NULL),
(10, 2, 'Level 2: Relevant DSA Concept', 'LIFO Stack data structure for nested matching.', NULL, NULL),
(10, 3, 'Level 3: Step-by-Step Logic', 'Push opening brackets onto stack. For closing brackets, check if stack is empty or top does not match; if so, return false. Valid if stack is empty at end.', NULL, NULL),
(10, 4, 'Level 4: Optimal Space-Time Analysis', 'Each character is pushed and popped at most once: O(N) time and O(N) auxiliary stack space in worst case.', 'O(N)', 'O(N)');

-- Hints for Problem 11: Daily Temperatures
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(11, 1, 'Level 1: Conceptual Nudge', 'You need the distance to the next greater element for each index.', NULL, NULL),
(11, 2, 'Level 2: Relevant DSA Concept', 'Monotonic Decreasing Stack storing indices.', NULL, NULL),
(11, 3, 'Level 3: Step-by-Step Logic', 'Iterate index i. While stack is not empty and temp[i] > temp[stack.top()], pop prevIndex and set result[prevIndex] = i - prevIndex. Push i onto stack.', NULL, NULL),
(11, 4, 'Level 4: Optimal Space-Time Analysis', 'Monotonic stack processes each index with at most one push and pop, achieving O(N) time and O(N) space.', 'O(N)', 'O(N)');

-- Hints for Problem 12: Binary Search
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(12, 1, 'Level 1: Conceptual Nudge', 'Since the array is sorted, comparing target with the middle element eliminates half of the elements.', NULL, NULL),
(12, 2, 'Level 2: Relevant DSA Concept', 'Binary Search (Divide and Conquer on monotonic array).', NULL, NULL),
(12, 3, 'Level 3: Step-by-Step Logic', 'Set left = 0, right = n - 1. While left <= right: mid = left + (right - left) / 2. If nums[mid] == target, return mid; if nums[mid] < target, left = mid + 1; else right = mid - 1.', NULL, NULL),
(12, 4, 'Level 4: Optimal Space-Time Analysis', 'Halving the search space at each iteration gives O(log N) time and O(1) space iteratively.', 'O(log N)', 'O(1)');

-- Hints for Problem 13: Maximum Depth of Binary Tree
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(13, 1, 'Level 1: Conceptual Nudge', 'The depth of any node is 1 plus the maximum depth of its left and right subtrees.', NULL, NULL),
(13, 2, 'Level 2: Relevant DSA Concept', 'Depth-First Search (DFS) tree recursion or BFS level-order traversal.', NULL, NULL),
(13, 3, 'Level 3: Step-by-Step Logic', 'Base case: if root is null, return 0. Recursive step: return 1 + max(maxDepth(root.left), maxDepth(root.right)).', NULL, NULL),
(13, 4, 'Level 4: Optimal Space-Time Analysis', 'Visits every node once for O(N) time and O(H) call stack space where H is tree height (O(log N) balanced, O(N) skewed).', 'O(N)', 'O(H)');

-- Hints for Problem 14: Lowest Common Ancestor
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(14, 1, 'Level 1: Conceptual Nudge', 'If current node matches p or q, or if p and q lie in different subtrees of current node, current node is the LCA.', NULL, NULL),
(14, 2, 'Level 2: Relevant DSA Concept', 'Post-order DFS traversal bubbling matching nodes upward.', NULL, NULL),
(14, 3, 'Level 3: Step-by-Step Logic', 'If root is null or equals p or q, return root. Recurse left and right. If both return non-null, root is LCA; else return whichever side is non-null.', NULL, NULL),
(14, 4, 'Level 4: Optimal Space-Time Analysis', 'Single bottom-up traversal runs in O(N) time and O(H) auxiliary space for recursion stack.', 'O(N)', 'O(H)');

-- Hints for Problem 15: Number of Islands
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(15, 1, 'Level 1: Conceptual Nudge', 'When encountering land (''1''), trigger a search that traverses and marks the entire connected component.', NULL, NULL),
(15, 2, 'Level 2: Relevant DSA Concept', 'Grid DFS or BFS traversal. Sinking visited land (changing ''1'' to ''0'') saves auxiliary memory.', NULL, NULL),
(15, 3, 'Level 3: Step-by-Step Logic', 'Loop over all grid cells. If cell is ''1'', increment islandCount and call dfs(r, c) to flip all 4-directionally reachable ''1''s to ''0''.', NULL, NULL),
(15, 4, 'Level 4: Optimal Space-Time Analysis', 'Each cell is visited a constant number of times: O(M * N) time and O(M * N) worst-case recursion stack space.', 'O(M * N)', 'O(M * N)');

-- Hints for Problem 16: Climbing Stairs
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(16, 1, 'Level 1: Conceptual Nudge', 'To reach step n, you must arrive from either step n - 1 or step n - 2.', NULL, NULL),
(16, 2, 'Level 2: Relevant DSA Concept', 'Dynamic Programming with optimal substructure: ways(n) = ways(n - 1) + ways(n - 2).', NULL, NULL),
(16, 3, 'Level 3: Step-by-Step Logic', 'Base cases: 1 way for n=1, 2 ways for n=2. Iteratively compute next = prev1 + prev2, then advance variables.', NULL, NULL),
(16, 4, 'Level 4: Optimal Space-Time Analysis', 'Space-optimized DP requires O(N) time and O(1) space, storing only the previous two step counts.', 'O(N)', 'O(1)');

-- Hints for Problem 17: Maximum Subarray
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(17, 1, 'Level 1: Conceptual Nudge', 'If running sum becomes negative, it hurts any future subarray, so reset the sum to the current element.', NULL, NULL),
(17, 2, 'Level 2: Relevant DSA Concept', 'Kadane''s Algorithm (Dynamic Programming).', NULL, NULL),
(17, 3, 'Level 3: Step-by-Step Logic', 'Initialize currentSum = nums[0], maxSum = nums[0]. For i from 1 to n - 1: currentSum = max(nums[i], currentSum + nums[i]); maxSum = max(maxSum, currentSum).', NULL, NULL),
(17, 4, 'Level 4: Optimal Space-Time Analysis', 'Single pass through array runs in O(N) time and O(1) extra space.', 'O(N)', 'O(1)');

-- Hints for Problem 18: Coin Change
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(18, 1, 'Level 1: Conceptual Nudge', 'Fewest coins for amount A is 1 plus the minimum coins needed for any (A - coin).', NULL, NULL),
(18, 2, 'Level 2: Relevant DSA Concept', 'Bottom-Up Dynamic Programming (Unbounded Knapsack).', NULL, NULL),
(18, 3, 'Level 3: Step-by-Step Logic', 'Initialize dp array of size amount + 1 with infinity sentinel, dp[0] = 0. For each coin, and for a from coin to amount: dp[a] = min(dp[a], 1 + dp[a - coin]).', NULL, NULL),
(18, 4, 'Level 4: Optimal Space-Time Analysis', 'Iterates over all amounts and coins in O(amount * len(coins)) time and O(amount) space.', 'O(A * C)', 'O(A)');

-- Hints for Problem 19: Edit Distance (VERIFIED HARD)
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(19, 1, 'Level 1: Conceptual Nudge', 'If characters match, cost is 0. If they differ, choose the minimum cost among insert, delete, or replace.', NULL, NULL),
(19, 2, 'Level 2: Relevant DSA Concept', '2D Dynamic Programming table dp[i][j] representing distance between word1[0..i] and word2[0..j].', NULL, NULL),
(19, 3, 'Level 3: Step-by-Step Logic', 'If word1[i-1] == word2[j-1], dp[i][j] = dp[i-1][j-1]. Otherwise dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]).', NULL, NULL),
(19, 4, 'Level 4: Optimal Space-Time Analysis', 'Fills matrix in O(M * N) time and O(M * N) space (or O(min(M, N)) with space optimization).', 'O(M * N)', 'O(M * N)');

-- Hints for Problem 20: Fibonacci Number
INSERT INTO hints (problem_id, level, title, content, time_complexity, space_complexity) VALUES
(20, 1, 'Level 1: Conceptual Nudge', 'Direct recursion recalculates the same values exponentially. Store intermediate results.', NULL, NULL),
(20, 2, 'Level 2: Relevant DSA Concept', 'Iterative Dynamic Programming maintaining the two most recent values.', NULL, NULL),
(20, 3, 'Level 3: Step-by-Step Logic', 'If n <= 1, return n. Initialize a = 0, b = 1. Loop i from 2 to n: c = a + b; a = b; b = c. Return b.', NULL, NULL),
(20, 4, 'Level 4: Optimal Space-Time Analysis', 'Iterative computation completes in O(N) time and strictly O(1) auxiliary memory.', 'O(N)', 'O(1)');

-- 4. Seed Language Starter Templates (Boilerplate skeletons only, no solutions)

-- Problem 1: Two Sum
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(1, 'Java', 'java', 'class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Write your solution here\n        return new int[0];\n    }\n}'),
(1, 'Python', 'python', 'class Solution:\n    def twoSum(self, nums: list[int], target: int) -> list[int]:\n        # Write your solution here\n        return []'),
(1, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    vector<int> twoSum(vector<int>& nums, int target) {\n        // Write your solution here\n        return {};\n    }\n};');

-- Problem 2: Best Time to Buy and Sell Stock
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(2, 'Java', 'java', 'class Solution {\n    public int maxProfit(int[] prices) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(2, 'Python', 'python', 'class Solution:\n    def maxProfit(self, prices: list[int]) -> int:\n        # Write your solution here\n        return 0'),
(2, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int maxProfit(vector<int>& prices) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 3: Container With Most Water
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(3, 'Java', 'java', 'class Solution {\n    public int maxArea(int[] height) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(3, 'Python', 'python', 'class Solution:\n    def maxArea(self, height: list[int]) -> int:\n        # Write your solution here\n        return 0'),
(3, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int maxArea(vector<int>& height) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 4: Trapping Rain Water (VERIFIED HARD)
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(4, 'Java', 'java', 'class Solution {\n    public int trap(int[] height) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(4, 'Python', 'python', 'class Solution:\n    def trap(self, height: list[int]) -> int:\n        # Write your solution here\n        return 0'),
(4, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int trap(vector<int>& height) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 5: Valid Palindrome
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(5, 'Java', 'java', 'class Solution {\n    public boolean isPalindrome(String s) {\n        // Write your solution here\n        return false;\n    }\n}'),
(5, 'Python', 'python', 'class Solution:\n    def isPalindrome(self, s: str) -> bool:\n        # Write your solution here\n        return False'),
(5, 'C++', 'cpp', '#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    bool isPalindrome(string s) {\n        // Write your solution here\n        return false;\n    }\n};');

-- Problem 6: Reverse a String
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(6, 'Java', 'java', 'class Solution {\n    public void reverseString(char[] s) {\n        // Write your solution here\n    }\n}'),
(6, 'Python', 'python', 'class Solution:\n    def reverseString(self, s: list[str]) -> None:\n        # Write your solution here\n        pass'),
(6, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    void reverseString(vector<char>& s) {\n        // Write your solution here\n    }\n};');

-- Problem 7: Longest Substring Without Repeating Characters
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(7, 'Java', 'java', 'class Solution {\n    public int lengthOfLongestSubstring(String s) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(7, 'Python', 'python', 'class Solution:\n    def lengthOfLongestSubstring(self, s: str) -> int:\n        # Write your solution here\n        return 0'),
(7, 'C++', 'cpp', '#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    int lengthOfLongestSubstring(string s) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 8: Reverse Linked List
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(8, 'Java', 'java', 'class Solution {\n    public ListNode reverseList(ListNode head) {\n        // Write your solution here\n        return null;\n    }\n}'),
(8, 'Python', 'python', 'class Solution:\n    def reverseList(self, head: ListNode) -> ListNode:\n        # Write your solution here\n        return None'),
(8, 'C++', 'cpp', 'class Solution {\npublic:\n    ListNode* reverseList(ListNode* head) {\n        // Write your solution here\n        return nullptr;\n    }\n};');

-- Problem 9: Merge k Sorted Lists (VERIFIED HARD)
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(9, 'Java', 'java', 'class Solution {\n    public ListNode mergeKLists(ListNode[] lists) {\n        // Write your solution here\n        return null;\n    }\n}'),
(9, 'Python', 'python', 'class Solution:\n    def mergeKLists(self, lists: list[ListNode]) -> ListNode:\n        # Write your solution here\n        return None'),
(9, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    ListNode* mergeKLists(vector<ListNode*>& lists) {\n        // Write your solution here\n        return nullptr;\n    }\n};');

-- Problem 10: Valid Parentheses
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(10, 'Java', 'java', 'class Solution {\n    public boolean isValid(String s) {\n        // Write your solution here\n        return false;\n    }\n}'),
(10, 'Python', 'python', 'class Solution:\n    def isValid(self, s: str) -> bool:\n        # Write your solution here\n        return False'),
(10, 'C++', 'cpp', '#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    bool isValid(string s) {\n        // Write your solution here\n        return false;\n    }\n};');

-- Problem 11: Daily Temperatures
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(11, 'Java', 'java', 'class Solution {\n    public int[] dailyTemperatures(int[] temperatures) {\n        // Write your solution here\n        return new int[0];\n    }\n}'),
(11, 'Python', 'python', 'class Solution:\n    def dailyTemperatures(self, temperatures: list[int]) -> list[int]:\n        # Write your solution here\n        return []'),
(11, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    vector<int> dailyTemperatures(vector<int>& temperatures) {\n        // Write your solution here\n        return {};\n    }\n};');

-- Problem 12: Binary Search
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(12, 'Java', 'java', 'class Solution {\n    public int search(int[] nums, int target) {\n        // Write your solution here\n        return -1;\n    }\n}'),
(12, 'Python', 'python', 'class Solution:\n    def search(self, nums: list[int], target: int) -> int:\n        # Write your solution here\n        return -1'),
(12, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int search(vector<int>& nums, int target) {\n        // Write your solution here\n        return -1;\n    }\n};');

-- Problem 13: Maximum Depth of Binary Tree
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(13, 'Java', 'java', 'class Solution {\n    public int maxDepth(TreeNode root) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(13, 'Python', 'python', 'class Solution:\n    def maxDepth(self, root: TreeNode) -> int:\n        # Write your solution here\n        return 0'),
(13, 'C++', 'cpp', 'class Solution {\npublic:\n    int maxDepth(TreeNode* root) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 14: Lowest Common Ancestor
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(14, 'Java', 'java', 'class Solution {\n    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {\n        // Write your solution here\n        return null;\n    }\n}'),
(14, 'Python', 'python', 'class Solution:\n    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> TreeNode:\n        # Write your solution here\n        return None'),
(14, 'C++', 'cpp', 'class Solution {\npublic:\n    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {\n        // Write your solution here\n        return nullptr;\n    }\n};');

-- Problem 15: Number of Islands
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(15, 'Java', 'java', 'class Solution {\n    public int numIslands(char[][] grid) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(15, 'Python', 'python', 'class Solution:\n    def numIslands(self, grid: list[list[str]]) -> int:\n        # Write your solution here\n        return 0'),
(15, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int numIslands(vector<vector<char>>& grid) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 16: Climbing Stairs
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(16, 'Java', 'java', 'class Solution {\n    public int climbStairs(int n) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(16, 'Python', 'python', 'class Solution:\n    def climbStairs(self, n: int) -> int:\n        # Write your solution here\n        return 0'),
(16, 'C++', 'cpp', 'class Solution {\npublic:\n    int climbStairs(int n) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 17: Maximum Subarray
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(17, 'Java', 'java', 'class Solution {\n    public int maxSubArray(int[] nums) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(17, 'Python', 'python', 'class Solution:\n    def maxSubArray(self, nums: list[int]) -> int:\n        # Write your solution here\n        return 0'),
(17, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int maxSubArray(vector<int>& nums) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 18: Coin Change
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(18, 'Java', 'java', 'class Solution {\n    public int coinChange(int[] coins, int amount) {\n        // Write your solution here\n        return -1;\n    }\n}'),
(18, 'Python', 'python', 'class Solution:\n    def coinChange(self, coins: list[int], amount: int) -> int:\n        # Write your solution here\n        return -1'),
(18, 'C++', 'cpp', '#include <vector>\nusing namespace std;\n\nclass Solution {\npublic:\n    int coinChange(vector<int>& coins, int amount) {\n        // Write your solution here\n        return -1;\n    }\n};');

-- Problem 19: Edit Distance (VERIFIED HARD)
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(19, 'Java', 'java', 'class Solution {\n    public int minDistance(String word1, String word2) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(19, 'Python', 'python', 'class Solution:\n    def minDistance(self, word1: str, word2: str) -> int:\n        # Write your solution here\n        return 0'),
(19, 'C++', 'cpp', '#include <string>\nusing namespace std;\n\nclass Solution {\npublic:\n    int minDistance(string word1, string word2) {\n        // Write your solution here\n        return 0;\n    }\n};');

-- Problem 20: Fibonacci Number
INSERT INTO language_options (problem_id, name, language_key, starter_template) VALUES
(20, 'Java', 'java', 'class Solution {\n    public int fib(int n) {\n        // Write your solution here\n        return 0;\n    }\n}'),
(20, 'Python', 'python', 'class Solution:\n    def fib(self, n: int) -> int:\n        # Write your solution here\n        return 0'),
(20, 'C++', 'cpp', 'class Solution {\npublic:\n    int fib(int n) {\n        // Write your solution here\n        return 0;\n    }\n};');
