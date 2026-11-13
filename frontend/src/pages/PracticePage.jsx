import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import {
  Play,
  CheckCircle2,
  Clock,
  Bookmark as BookmarkIcon,
  StickyNote,
  Sparkles,
  Code2,
  Tag,
  Check,
  AlertCircle,
  Terminal,
  Zap,
} from 'lucide-react';
import { progressApi } from '../api/progressApi';
import { bookmarksApi } from '../api/bookmarksApi';
import { DifficultyBadge, StatusBadge, TopicBadge } from '../components/common/Badge';
import { NoteModal } from '../components/notes/NoteModal';
import { useNotes } from '../context/NotesContext';

const CURATED_PROBLEMS = [
  {
    id: 'two-sum',
    title: 'Two Sum',
    topic: 'ARRAYS',
    difficulty: 'EASY',
    description:
      'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.',
    defaultCode: 'class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            map.put(nums[i], i);\n        }\n        return new int[0];\n    }\n}',
    mnemonicHint: 'Complement Lookup: target - current in single pass hash table.',
  },
  {
    id: 'reverse-linked-list',
    title: 'Reverse Linked List',
    topic: 'LINKED_LIST',
    difficulty: 'EASY',
    description: 'Given the head of a singly linked list, reverse the list, and return the reversed list.',
    defaultCode: 'class Solution {\n    public ListNode reverseList(ListNode head) {\n        ListNode prev = null;\n        ListNode curr = head;\n        while (curr != null) {\n            ListNode next = curr.next;\n            curr.next = prev;\n            prev = curr;\n            curr = next;\n        }\n        return prev;\n    }\n}',
    mnemonicHint: 'Three Pointers Dance: prev, curr, next. Point backwards, slide right.',
  },
  {
    id: 'coin-change',
    title: 'Coin Change',
    topic: 'DYNAMIC_PROGRAMMING',
    difficulty: 'MEDIUM',
    description:
      'You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.',
    defaultCode: 'class Solution {\n    public int coinChange(int[] coins, int amount) {\n        int[] dp = new int[amount + 1];\n        Arrays.fill(dp, amount + 1);\n        dp[0] = 0;\n        for (int a = 1; a <= amount; a++) {\n            for (int c : coins) {\n                if (a - c >= 0) {\n                    dp[a] = Math.min(dp[a], 1 + dp[a - c]);\n                }\n            }\n        }\n        return dp[amount] > amount ? -1 : dp[amount];\n    }\n}',
    mnemonicHint: 'Unbounded Knapsack: dp[amount] = min(dp[amount], 1 + dp[amount - coin]).',
  },
  {
    id: 'number-of-islands',
    title: 'Number of Islands',
    topic: 'GRAPHS',
    difficulty: 'MEDIUM',
    description:
      'Given an m x n 2D binary grid grid which represents a map of 1s (land) and 0s (water), return the number of islands.',
    defaultCode: 'class Solution {\n    public int numIslands(char[][] grid) {\n        int count = 0;\n        for (int r = 0; r < grid.length; r++) {\n            for (int c = 0; c < grid[0].length; c++) {\n                if (grid[r][c] == \'1\') {\n                    count++;\n                    dfs(grid, r, c);\n                }\n            }\n        }\n        return count;\n    }\n    private void dfs(char[][] g, int r, int c) {\n        if (r < 0 || r >= g.length || c < 0 || c >= g[0].length || g[r][c] != \'1\') return;\n        g[r][c] = \'0\'; // sink island\n        dfs(g, r + 1, c); dfs(g, r - 1, c); dfs(g, r, c + 1); dfs(g, r, c - 1);\n    }\n}',
    mnemonicHint: 'Sink Islands In-Place: Flip 1 to 0 during DFS to avoid auxiliary memory.',
  },
  {
    id: 'trapping-rain-water',
    title: 'Trapping Rain Water',
    topic: 'ARRAYS',
    difficulty: 'HARD',
    description:
      'Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.',
    defaultCode: 'class Solution {\n    public int trap(int[] height) {\n        int left = 0, right = height.length - 1;\n        int maxLeft = 0, maxRight = 0, trapped = 0;\n        while (left < right) {\n            if (height[left] < height[right]) {\n                if (height[left] >= maxLeft) maxLeft = height[left];\n                else trapped += maxLeft - height[left];\n                left++;\n            } else {\n                if (height[right] >= maxRight) maxRight = height[right];\n                else trapped += maxRight - height[right];\n                right--;\n            }\n        }\n        return trapped;\n    }\n}',
    mnemonicHint: 'Inward Squeeze: Move the pointer on the lower wall side inward.',
  },
];

export const PracticePage = () => {
  const [searchParams] = useSearchParams();
  const requestedProblemId = searchParams.get('problem');

  const [selectedProblem, setSelectedProblem] = useState(
    CURATED_PROBLEMS.find((p) => p.id === requestedProblemId) || CURATED_PROBLEMS[0]
  );

  const [code, setCode] = useState(selectedProblem.defaultCode);
  const [timeSpentSeconds, setTimeSpentSeconds] = useState(300);
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState(null);

  // Bookmark state
  const [isBookmarked, setIsBookmarked] = useState(false);
  const [bookmarkInsight, setBookmarkInsight] = useState('');
  const [bookmarkTags, setBookmarkTags] = useState('');
  const [showBookmarkForm, setShowBookmarkForm] = useState(false);

  // Note Modal state
  const [noteModalOpen, setNoteModalOpen] = useState(false);
  const [noteModalType, setNoteModalType] = useState('NOTE');

  // Check bookmark status on problem change
  useEffect(() => {
    let isMounted = true;
    bookmarksApi
      .checkBookmark(selectedProblem.id)
      .then((res) => {
        if (isMounted && res.success) {
          setIsBookmarked(res.data);
        }
      })
      .catch(() => {});
    return () => {
      isMounted = false;
    };
  }, [selectedProblem.id]);

  const handleSelectProblem = (p) => {
    setSelectedProblem(p);
    setCode(p.defaultCode);
    setFeedback(null);
    setShowBookmarkForm(false);
  };

  const handleRecordAttempt = async (status) => {
    setSubmitting(true);
    setFeedback(null);
    try {
      const res = await progressApi.recordAttempt({
        problemId: selectedProblem.id,
        topic: selectedProblem.topic,
        difficulty: selectedProblem.difficulty,
        status,
        timeSpentSeconds,
        incrementAttempts: true,
      });

      if (res.success) {
        setFeedback({
          type: 'success',
          message:
            status === 'SOLVED'
              ? '🎉 Problem marked as SOLVED! Streak and stats updated.'
              : 'Progress saved as IN_PROGRESS.',
        });
      }
    } catch (err) {
      setFeedback({
        type: 'error',
        message: err.response?.data?.message || 'Failed to record progress',
      });
    } finally {
      setSubmitting(false);
    }
  };

  const handleSaveBookmark = async () => {
    try {
      await bookmarksApi.createBookmark({
        problemId: selectedProblem.id,
        insight: bookmarkInsight || selectedProblem.mnemonicHint,
        tags: bookmarkTags || selectedProblem.topic.toLowerCase(),
      });
      setIsBookmarked(true);
      setShowBookmarkForm(false);
      setFeedback({
        type: 'success',
        message: '📌 Saved to your bookmarks with personal insight!',
      });
    } catch (err) {
      setFeedback({
        type: 'error',
        message: 'Failed to save bookmark',
      });
    }
  };

  const handleOpenNote = (type) => {
    setNoteModalType(type);
    setNoteModalOpen(true);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-xl sm:text-2xl font-extrabold text-white flex items-center space-x-2">
            <Terminal className="w-6 h-6 text-indigo-400" />
            <span>Interactive Problem Runner</span>
          </h1>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            Solve DSA problems, attach floating sticky notes or mnemonics, and record attempts to your streak.
          </p>
        </div>

        {/* Quick Note/Mnemonic Actions */}
        <div className="flex items-center space-x-2">
          <button
            onClick={() => handleOpenNote('NOTE')}
            className="flex items-center space-x-1.5 px-3 py-2 rounded-xl text-xs font-semibold bg-amber-500/15 text-amber-300 border border-amber-500/30 hover:bg-amber-500/25 transition-all"
          >
            <StickyNote className="w-3.5 h-3.5" />
            <span>Pin Study Note</span>
          </button>
          <button
            onClick={() => handleOpenNote('MNEMONIC')}
            className="flex items-center space-x-1.5 px-3.5 py-2 rounded-xl text-xs font-bold bg-purple-600 text-white shadow-lg shadow-purple-600/30 hover:bg-purple-500 transition-all"
          >
            <Sparkles className="w-3.5 h-3.5" />
            <span>Add Mnemonic</span>
          </button>
        </div>
      </div>

      {/* Main Grid: Problem Selector + Code Workspace */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Left Column: Problem List */}
        <div className="lg:col-span-1 space-y-3">
          <div className="p-4 rounded-2xl bg-slate-900 border border-slate-800 space-y-3">
            <h3 className="text-xs font-bold text-slate-300 uppercase tracking-wider">
              Curated Problem Set
            </h3>
            <div className="space-y-1.5">
              {CURATED_PROBLEMS.map((p) => {
                const isSelected = p.id === selectedProblem.id;
                return (
                  <button
                    key={p.id}
                    onClick={() => handleSelectProblem(p)}
                    className={`w-full text-left p-2.5 rounded-xl text-xs transition-all flex items-center justify-between ${
                      isSelected
                        ? 'bg-indigo-600/20 text-indigo-200 border border-indigo-500/50 shadow-md font-semibold'
                        : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                    }`}
                  >
                    <span className="truncate pr-2">{p.title}</span>
                    <DifficultyBadge difficulty={p.difficulty} />
                  </button>
                );
              })}
            </div>
          </div>

          {/* Quick Mnemonic Preview Card */}
          <div className="p-4 rounded-2xl bg-gradient-to-br from-purple-950/40 via-slate-900 to-slate-900 border border-purple-500/30 space-y-2">
            <div className="flex items-center space-x-1.5 text-purple-300 text-xs font-bold">
              <Sparkles className="w-3.5 h-3.5 text-purple-400" />
              <span>Recommended Mnemonic</span>
            </div>
            <p className="text-xs text-slate-300 italic leading-relaxed">
              "{selectedProblem.mnemonicHint}"
            </p>
          </div>
        </div>

        {/* Right Columns: Problem Workspace */}
        <div className="lg:col-span-3 space-y-4">
          {/* Problem Details Card */}
          <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800 space-y-4">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
              <div className="space-y-1">
                <div className="flex items-center space-x-2.5">
                  <h2 className="text-lg font-bold text-white">{selectedProblem.title}</h2>
                  <DifficultyBadge difficulty={selectedProblem.difficulty} />
                  <TopicBadge topic={selectedProblem.topic} />
                </div>
                <p className="text-xs font-mono text-slate-500">problemId: {selectedProblem.id}</p>
              </div>

              {/* Bookmark Toggle */}
              <button
                onClick={() => setShowBookmarkForm(!showBookmarkForm)}
                className={`flex items-center space-x-1.5 px-3 py-1.5 rounded-xl text-xs font-semibold border transition-all ${
                  isBookmarked
                    ? 'bg-purple-500/15 text-purple-300 border-purple-500/30'
                    : 'bg-slate-800 text-slate-400 border-slate-700 hover:text-white'
                }`}
              >
                <BookmarkIcon className={`w-3.5 h-3.5 ${isBookmarked ? 'fill-purple-400' : ''}`} />
                <span>{isBookmarked ? 'Bookmarked' : 'Add Bookmark'}</span>
              </button>
            </div>

            <p className="text-xs text-slate-300 leading-relaxed bg-slate-950/50 p-3.5 rounded-xl border border-slate-800">
              {selectedProblem.description}
            </p>

            {/* Bookmark Drawer/Form if open */}
            {showBookmarkForm && (
              <div className="p-4 rounded-xl bg-purple-950/20 border border-purple-500/30 space-y-3">
                <h4 className="text-xs font-bold text-purple-300">Save Problem Insight & Tags</h4>
                <textarea
                  rows={2}
                  value={bookmarkInsight}
                  onChange={(e) => setBookmarkInsight(e.target.value)}
                  placeholder={`Insight or trick (default: ${selectedProblem.mnemonicHint})`}
                  className="w-full p-2.5 text-xs bg-slate-900 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:border-purple-500"
                />
                <div className="flex items-center gap-2">
                  <input
                    type="text"
                    value={bookmarkTags}
                    onChange={(e) => setBookmarkTags(e.target.value)}
                    placeholder="Tags comma-separated (e.g. hashmap, classic)"
                    className="flex-1 p-2 text-xs bg-slate-900 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:border-purple-500"
                  />
                  <button
                    onClick={handleSaveBookmark}
                    className="px-4 py-2 text-xs font-bold text-white bg-purple-600 hover:bg-purple-500 rounded-lg shadow-md"
                  >
                    Save
                  </button>
                </div>
              </div>
            )}

            {/* Feedback message */}
            {feedback && (
              <div
                className={`p-3 rounded-xl text-xs flex items-center space-x-2 ${
                  feedback.type === 'success'
                    ? 'bg-emerald-500/10 border border-emerald-500/20 text-emerald-300'
                    : 'bg-rose-500/10 border border-rose-500/20 text-rose-300'
                }`}
              >
                {feedback.type === 'success' ? (
                  <Check className="w-4 h-4 flex-shrink-0" />
                ) : (
                  <AlertCircle className="w-4 h-4 flex-shrink-0" />
                )}
                <span>{feedback.message}</span>
              </div>
            )}

            {/* Code Editor Mockup */}
            <div className="rounded-xl overflow-hidden border border-slate-800 bg-slate-950">
              <div className="flex items-center justify-between px-4 py-2 bg-slate-900/90 border-b border-slate-800 text-[11px] text-slate-400">
                <div className="flex items-center space-x-2">
                  <Code2 className="w-3.5 h-3.5 text-indigo-400" />
                  <span className="font-mono">Solution.java</span>
                </div>
                <span>Java 17</span>
              </div>
              <textarea
                value={code}
                onChange={(e) => setCode(e.target.value)}
                rows={12}
                className="w-full p-4 font-mono text-xs bg-transparent text-slate-200 resize-none focus:outline-none leading-relaxed"
                spellCheck="false"
              />
            </div>

            {/* Simulation & Attempt Recorder Controls */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pt-2">
              <div className="flex items-center space-x-3 text-xs text-slate-400">
                <Clock className="w-4 h-4 text-slate-500" />
                <span>Simulated time spent:</span>
                <select
                  value={timeSpentSeconds}
                  onChange={(e) => setTimeSpentSeconds(Number(e.target.value))}
                  className="bg-slate-800 border border-slate-700 rounded-lg px-2 py-1 text-white text-xs focus:outline-none"
                >
                  <option value={120}>2 minutes</option>
                  <option value={300}>5 minutes</option>
                  <option value={600}>10 minutes</option>
                  <option value={1200}>20 minutes</option>
                  <option value={1800}>30 minutes</option>
                </select>
              </div>

              <div className="flex items-center space-x-2">
                <button
                  type="button"
                  disabled={submitting}
                  onClick={() => handleRecordAttempt('IN_PROGRESS')}
                  className="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 transition-colors disabled:opacity-50"
                >
                  Mark In Progress
                </button>
                <button
                  type="button"
                  disabled={submitting}
                  onClick={() => handleRecordAttempt('SOLVED')}
                  className="flex items-center space-x-1.5 px-5 py-2 rounded-xl text-xs font-bold text-white bg-emerald-600 hover:bg-emerald-500 shadow-lg shadow-emerald-600/30 transition-all disabled:opacity-50"
                >
                  <CheckCircle2 className="w-4 h-4" />
                  <span>Submit & Mark Solved</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <NoteModal
        isOpen={noteModalOpen}
        onClose={() => setNoteModalOpen(false)}
        defaultProblemId={selectedProblem.id}
        initialType={noteModalType}
      />
    </div>
  );
};
