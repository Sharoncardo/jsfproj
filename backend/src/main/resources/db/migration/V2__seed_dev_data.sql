-- Flyway Migration V2: Seed Development Data

-- Passwords are BCrypt hashes of "Password123!"
-- BCrypt: $2a$10$wK1F5N4k4vV7wO4Yq8k24evH9bNqvZk/F3/24a1m6xYJjQnE8tMvi (verified BCrypt for Password123!)
-- Seed Users (Password: Password123!)
INSERT INTO users (id, username, email, password_hash, role, display_name, avatar_url, created_at, updated_at, last_login_at, enabled)
VALUES 
('a0000000-0000-0000-0000-000000000001', 'demo_user', 'demo@example.com', '$2a$10$esoYtKuKdgNv5eu59/2UR.A/zcP2PpwHdAeaqNgu/7KVB7zxBJNC.', 'USER', 'Alex Rivera', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
('a0000000-0000-0000-0000-000000000002', 'admin', 'admin@example.com', '$2a$10$esoYtKuKdgNv5eu59/2UR.A/zcP2PpwHdAeaqNgu/7KVB7zxBJNC.', 'ADMIN', 'System Admin', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- Seed Progress Tracks for demo_user across various topics & difficulties
INSERT INTO progress_tracks (id, user_id, problem_id, topic, difficulty, status, attempts, time_spent_seconds, solved_at, last_attempted_at)
VALUES
('b0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', 'two-sum', 'ARRAYS', 'EASY', 'SOLVED', 1, 420, CURRENT_TIMESTAMP - INTERVAL '5' DAY, CURRENT_TIMESTAMP - INTERVAL '5' DAY),
('b0000000-0000-0000-0000-000000000002', 'a0000000-0000-0000-0000-000000000001', 'best-time-to-buy-and-sell-stock', 'ARRAYS', 'EASY', 'SOLVED', 2, 600, CURRENT_TIMESTAMP - INTERVAL '4' DAY, CURRENT_TIMESTAMP - INTERVAL '4' DAY),
('b0000000-0000-0000-0000-000000000003', 'a0000000-0000-0000-0000-000000000001', 'container-with-most-water', 'ARRAYS', 'MEDIUM', 'SOLVED', 3, 1100, CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY),
('b0000000-0000-0000-0000-000000000004', 'a0000000-0000-0000-0000-000000000001', 'trapping-rain-water', 'ARRAYS', 'HARD', 'IN_PROGRESS', 4, 1800, NULL, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
('b0000000-0000-0000-0000-000000000005', 'a0000000-0000-0000-0000-000000000001', 'valid-palindrome', 'STRINGS', 'EASY', 'SOLVED', 1, 310, CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY),
('b0000000-0000-0000-0000-000000000006', 'a0000000-0000-0000-0000-000000000001', 'longest-substring-without-repeating-characters', 'STRINGS', 'MEDIUM', 'SOLVED', 2, 950, CURRENT_TIMESTAMP - INTERVAL '2' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
('b0000000-0000-0000-0000-000000000007', 'a0000000-0000-0000-0000-000000000001', 'reverse-linked-list', 'LINKED_LIST', 'EASY', 'SOLVED', 1, 240, CURRENT_TIMESTAMP - INTERVAL '2' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
('b0000000-0000-0000-0000-000000000008', 'a0000000-0000-0000-0000-000000000001', 'merge-k-sorted-lists', 'LINKED_LIST', 'HARD', 'SOLVED', 4, 2100, CURRENT_TIMESTAMP - INTERVAL '1' DAY, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
('b0000000-0000-0000-0000-000000000009', 'a0000000-0000-0000-0000-000000000001', 'valid-parentheses', 'STACK_QUEUE', 'EASY', 'SOLVED', 1, 290, CURRENT_TIMESTAMP - INTERVAL '1' DAY, CURRENT_TIMESTAMP - INTERVAL '1' DAY),
('b0000000-0000-0000-0000-000000000010', 'a0000000-0000-0000-0000-000000000001', 'daily-temperatures', 'STACK_QUEUE', 'MEDIUM', 'SOLVED', 2, 850, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000011', 'a0000000-0000-0000-0000-000000000001', 'maximum-depth-of-binary-tree', 'TREES', 'EASY', 'SOLVED', 1, 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000012', 'a0000000-0000-0000-0000-000000000001', 'lowest-common-ancestor', 'TREES', 'MEDIUM', 'SOLVED', 3, 1200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000013', 'a0000000-0000-0000-0000-000000000001', 'number-of-islands', 'GRAPHS', 'MEDIUM', 'SOLVED', 2, 720, CURRENT_TIMESTAMP - INTERVAL '2' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY),
('b0000000-0000-0000-0000-000000000014', 'a0000000-0000-0000-0000-000000000001', 'climbing-stairs', 'DYNAMIC_PROGRAMMING', 'EASY', 'SOLVED', 1, 210, CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY),
('b0000000-0000-0000-0000-000000000015', 'a0000000-0000-0000-0000-000000000001', 'coin-change', 'DYNAMIC_PROGRAMMING', 'MEDIUM', 'SOLVED', 3, 1400, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b0000000-0000-0000-0000-000000000016', 'a0000000-0000-0000-0000-000000000001', 'edit-distance', 'DYNAMIC_PROGRAMMING', 'HARD', 'SOLVED', 5, 2600, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed Sticky Notes & Mnemonics
INSERT INTO sticky_notes (id, user_id, problem_id, title, content, type, color, position_x, position_y, width, height, pinned, created_at, updated_at)
VALUES
('c0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', 'reverse-linked-list', 'Floyd''s Cycle Pointer', 'Fast moves 2 steps, slow moves 1. Meeting point confirms cycle. Reset one pointer to head to discover the cycle start node!', 'MNEMONIC', '#FEF08A', 80.0, 100.0, 280.0, 220.0, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000002', 'a0000000-0000-0000-0000-000000000001', 'coin-change', 'DP State Recurrence', 'dp[amount] = min(dp[amount], 1 + dp[amount - coin]). Remember to initialize dp array with amount + 1 as infinity sentinel.', 'MNEMONIC', '#BAE6FD', 390.0, 100.0, 290.0, 220.0, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000003', 'a0000000-0000-0000-0000-000000000001', 'number-of-islands', 'Sink Island Optimization', 'In-place grid modification: replace ''1'' with ''0'' during BFS/DFS visit to avoid allocating O(M*N) auxiliary visited hash set.', 'NOTE', '#BBF7D0', 710.0, 100.0, 280.0, 220.0, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed Bookmarks
INSERT INTO bookmarks (id, user_id, problem_id, insight, tags, created_at)
VALUES
('d0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', 'trapping-rain-water', 'Two-pointer technique tracks leftMax and rightMax on the fly, eliminating O(N) auxiliary space.', 'two-pointers,greedy,hard', CURRENT_TIMESTAMP),
('d0000000-0000-0000-0000-000000000002', 'a0000000-0000-0000-0000-000000000001', 'merge-k-sorted-lists', 'Min-heap of size K yields O(N log K) time complexity where N is the total number of nodes across all lists.', 'heap,linked-list,hard', CURRENT_TIMESTAMP),
('d0000000-0000-0000-0000-000000000003', 'a0000000-0000-0000-0000-000000000001', 'coin-change', 'Bottom-up DP handles denomination iterations cleanly. Check edge cases where amount == 0.', 'dp,classic', CURRENT_TIMESTAMP);
