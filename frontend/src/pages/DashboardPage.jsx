import React, { useState, useEffect, useCallback } from 'react';
import {
  Flame,
  CheckCircle2,
  Percent,
  Bookmark as BookmarkIcon,
  TrendingUp,
  BarChart3,
  PieChart as PieIcon,
  Calendar,
  Layers,
  ChevronRight,
  Filter,
  X
} from 'lucide-react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  AreaChart,
  Area,
} from 'recharts';
import { progressApi } from '../api/progressApi';
import { bookmarksApi } from '../api/bookmarksApi';
import { useAuth } from '../context/AuthContext';
import { DashboardSkeleton } from '../components/common/Skeleton';
import { DifficultyBadge, StatusBadge, TopicBadge } from '../components/common/Badge';

const DIFFICULTY_COLORS = {
  Easy: '#10b981',   // emerald-500
  Medium: '#f59e0b', // amber-500
  Hard: '#f43f5e',   // rose-500
};

export const DashboardPage = () => {
  const { user } = useAuth();
  const [summary, setSummary] = useState(null);
  const [bookmarkCount, setBookmarkCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Topic Drill-down Modal State
  const [selectedTopic, setSelectedTopic] = useState(null);
  const [topicProblems, setTopicProblems] = useState([]);
  const [loadingTopicDetails, setLoadingTopicDetails] = useState(false);

  const fetchDashboardData = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const [sumRes, bmRes] = await Promise.all([
        progressApi.getSummary(),
        bookmarksApi.getBookmarks(0, 1),
      ]);

      if (sumRes.success) {
        setSummary(sumRes.data);
      }
      if (bmRes.success) {
        setBookmarkCount(bmRes.data.totalElements || 0);
      }
    } catch (err) {
      console.error('Failed to load dashboard metrics:', err);
      setError('Could not load progress statistics. Please refresh or check connection.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchDashboardData();
  }, [fetchDashboardData]);

  // Handle Drill-down when clicking a topic
  const handleTopicClick = async (topicStat) => {
    setSelectedTopic(topicStat);
    setLoadingTopicDetails(true);
    try {
      const res = await progressApi.getProgressList({ topic: topicStat.topic, size: 50 });
      if (res.success && res.data) {
        setTopicProblems(res.data.content || []);
      }
    } catch (err) {
      console.error('Failed to load topic problems:', err);
    } finally {
      setLoadingTopicDetails(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <DashboardSkeleton />
      </div>
    );
  }

  if (error || !summary) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-16 text-center">
        <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 max-w-md mx-auto mb-4 text-xs">
          {error || 'Unable to retrieve your progress summary.'}
        </div>
        <button
          onClick={fetchDashboardData}
          className="px-4 py-2 text-xs font-bold text-white bg-indigo-600 rounded-xl hover:bg-indigo-500"
        >
          Try Again
        </button>
      </div>
    );
  }

  // Prep chart data
  const topicChartData = summary.solvedByTopic.map((t) => ({
    name: t.displayName.split(' ')[0], // Short name for axis
    fullName: t.displayName,
    solved: t.solvedCount,
    attempted: t.attemptedCount,
    topic: t.topic,
    stat: t,
  }));

  const difficultyData = [
    { name: 'Easy', value: summary.solvedByDifficulty.easySolved, color: DIFFICULTY_COLORS.Easy },
    { name: 'Medium', value: summary.solvedByDifficulty.mediumSolved, color: DIFFICULTY_COLORS.Medium },
    { name: 'Hard', value: summary.solvedByDifficulty.hardSolved, color: DIFFICULTY_COLORS.Hard },
  ].filter((d) => d.value > 0);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8 animate-fade-in">
      {/* Welcome Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-6 rounded-2xl bg-gradient-to-r from-slate-900 via-indigo-950/40 to-slate-900 border border-slate-800">
        <div>
          <h1 className="text-xl sm:text-2xl font-extrabold text-white tracking-tight">
            Welcome back, {user?.displayName || user?.username}! 👋
          </h1>
          <p className="text-xs sm:text-sm text-slate-400 mt-1">
            You've solved <span className="font-bold text-emerald-400">{summary.totalSolved}</span> problems. Keep your momentum going!
          </p>
        </div>
        <div className="flex items-center space-x-2 bg-slate-950/60 p-2.5 rounded-xl border border-slate-800 self-start sm:self-auto">
          <div className="p-2 rounded-lg bg-amber-500/10 text-amber-400 animate-pulse-slow">
            <Flame className="w-5 h-5 fill-amber-500/30" />
          </div>
          <div>
            <div className="text-xs text-slate-400">Current Streak</div>
            <div className="text-base font-extrabold text-white font-mono">
              {summary.currentStreakDays} {summary.currentStreakDays === 1 ? 'Day' : 'Days'}
            </div>
          </div>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Solved */}
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 flex items-center justify-between hover:border-slate-700 transition-colors">
          <div>
            <p className="text-xs font-semibold text-slate-400">Total Solved</p>
            <h3 className="text-2xl font-extrabold text-white font-mono mt-1">
              {summary.totalSolved}
            </h3>
            <p className="text-[11px] text-slate-500 mt-0.5">
              across {summary.solvedByTopic.length} categories
            </p>
          </div>
          <div className="p-3 bg-emerald-500/10 text-emerald-400 rounded-xl">
            <CheckCircle2 className="w-6 h-6" />
          </div>
        </div>

        {/* Current & Longest Streak */}
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 flex items-center justify-between hover:border-slate-700 transition-colors">
          <div>
            <p className="text-xs font-semibold text-slate-400">Active Streak</p>
            <h3 className="text-2xl font-extrabold text-amber-400 font-mono mt-1">
              {summary.currentStreakDays}d
            </h3>
            <p className="text-[11px] text-slate-500 mt-0.5">
              Longest: <span className="text-slate-300 font-semibold">{summary.longestStreakDays} days</span>
            </p>
          </div>
          <div className="p-3 bg-amber-500/10 text-amber-400 rounded-xl">
            <Flame className="w-6 h-6 fill-amber-500/30" />
          </div>
        </div>

        {/* Completion % */}
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 flex items-center justify-between hover:border-slate-700 transition-colors">
          <div>
            <p className="text-xs font-semibold text-slate-400">Mastery Progress</p>
            <h3 className="text-2xl font-extrabold text-indigo-400 font-mono mt-1">
              {summary.overallCompletionPercentage}%
            </h3>
            <p className="text-[11px] text-slate-500 mt-0.5">Curated target curriculum</p>
          </div>
          <div className="p-3 bg-indigo-500/10 text-indigo-400 rounded-xl">
            <Percent className="w-6 h-6" />
          </div>
        </div>

        {/* Bookmarks */}
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 flex items-center justify-between hover:border-slate-700 transition-colors">
          <div>
            <p className="text-xs font-semibold text-slate-400">Saved Bookmarks</p>
            <h3 className="text-2xl font-extrabold text-purple-400 font-mono mt-1">
              {bookmarkCount}
            </h3>
            <p className="text-[11px] text-slate-500 mt-0.5">Personal insights & mnemonics</p>
          </div>
          <div className="p-3 bg-purple-500/10 text-purple-400 rounded-xl">
            <BookmarkIcon className="w-6 h-6 fill-purple-500/30" />
          </div>
        </div>
      </div>

      {/* Main Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Solved Problems by Topic Bar Chart */}
        <div className="lg:col-span-2 p-6 rounded-2xl bg-slate-900 border border-slate-800">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center space-x-2">
              <BarChart3 className="w-4 h-4 text-indigo-400" />
              <h3 className="text-sm font-bold text-white">Solved Problems by Topic</h3>
            </div>
            <span className="text-[11px] text-slate-400">
              Click any bar to drill down
            </span>
          </div>

          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={topicChartData} margin={{ top: 10, right: 10, left: -20, bottom: 20 }}>
                <XAxis
                  dataKey="name"
                  tick={{ fill: '#94a3b8', fontSize: 11 }}
                  interval={0}
                  angle={-25}
                  textAnchor="end"
                />
                <YAxis tick={{ fill: '#94a3b8', fontSize: 11 }} allowDecimals={false} />
                <Tooltip
                  content={({ active, payload }) => {
                    if (active && payload && payload.length) {
                      const data = payload[0].payload;
                      return (
                        <div className="p-3 bg-slate-950 border border-slate-800 rounded-xl shadow-xl text-xs space-y-1">
                          <p className="font-bold text-white">{data.fullName}</p>
                          <p className="text-emerald-400">Solved: {data.solved}</p>
                          <p className="text-slate-400">Attempted: {data.attempted}</p>
                          <p className="text-[10px] text-indigo-300 font-medium">Click to view problems</p>
                        </div>
                      );
                    }
                    return null;
                  }}
                />
                <Bar
                  dataKey="solved"
                  fill="#6366f1"
                  radius={[6, 6, 0, 0]}
                  cursor="pointer"
                  onClick={(entry) => handleTopicClick(entry.stat)}
                />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Difficulty Donut Chart */}
        <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800 flex flex-col justify-between">
          <div className="flex items-center space-x-2 mb-2">
            <PieIcon className="w-4 h-4 text-amber-400" />
            <h3 className="text-sm font-bold text-white">Difficulty Breakdown</h3>
          </div>

          <div className="h-48 w-full relative flex items-center justify-center">
            {difficultyData.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={difficultyData}
                    innerRadius={55}
                    outerRadius={80}
                    paddingAngle={4}
                    dataKey="value"
                  >
                    {difficultyData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip
                    content={({ active, payload }) => {
                      if (active && payload && payload.length) {
                        const d = payload[0].payload;
                        return (
                          <div className="p-2.5 bg-slate-950 border border-slate-800 rounded-lg text-xs font-semibold text-white shadow-xl">
                            <span style={{ color: d.color }}>{d.name}:</span> {d.value} solved
                          </div>
                        );
                      }
                      return null;
                    }}
                  />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <div className="text-xs text-slate-500">No solved problems yet</div>
            )}
            {/* Center Total */}
            <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
              <span className="text-2xl font-extrabold text-white font-mono">{summary.totalSolved}</span>
              <span className="text-[10px] uppercase font-semibold text-slate-500 tracking-wider">Solved</span>
            </div>
          </div>

          {/* Difficulty Legend */}
          <div className="grid grid-cols-3 gap-2 pt-2 border-t border-slate-800 text-center text-xs">
            <div>
              <span className="inline-block w-2 h-2 rounded-full bg-emerald-400 mr-1" />
              <span className="text-slate-400">Easy</span>
              <div className="font-bold text-white font-mono">{summary.solvedByDifficulty.easySolved}</div>
            </div>
            <div>
              <span className="inline-block w-2 h-2 rounded-full bg-amber-400 mr-1" />
              <span className="text-slate-400">Med</span>
              <div className="font-bold text-white font-mono">{summary.solvedByDifficulty.mediumSolved}</div>
            </div>
            <div>
              <span className="inline-block w-2 h-2 rounded-full bg-rose-400 mr-1" />
              <span className="text-slate-400">Hard</span>
              <div className="font-bold text-white font-mono">{summary.solvedByDifficulty.hardSolved}</div>
            </div>
          </div>
        </div>
      </div>

      {/* Daily Activity Chart (Last 30 Days) */}
      <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-2">
            <Calendar className="w-4 h-4 text-emerald-400" />
            <h3 className="text-sm font-bold text-white">Daily Practice Activity (Last 30 Days)</h3>
          </div>
          <span className="text-xs font-mono text-emerald-400 font-semibold">
            {summary.last30DaysActivity.reduce((acc, curr) => acc + curr.count, 0)} actions
          </span>
        </div>

        <div className="h-44 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={summary.last30DaysActivity} margin={{ top: 10, right: 10, left: -25, bottom: 0 }}>
              <defs>
                <linearGradient id="activityGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#10b981" stopOpacity={0.4} />
                  <stop offset="95%" stopColor="#10b981" stopOpacity={0.0} />
                </linearGradient>
              </defs>
              <XAxis
                dataKey="date"
                tick={{ fill: '#64748b', fontSize: 10 }}
                tickFormatter={(val) => val.slice(5)} // MM-DD
              />
              <YAxis tick={{ fill: '#64748b', fontSize: 10 }} allowDecimals={false} />
              <Tooltip
                content={({ active, payload }) => {
                  if (active && payload && payload.length) {
                    const item = payload[0].payload;
                    return (
                      <div className="p-2.5 bg-slate-950 border border-slate-800 rounded-lg text-xs shadow-xl">
                        <p className="text-slate-400">{item.date}</p>
                        <p className="font-bold text-emerald-400">{item.count} attempts / solved</p>
                      </div>
                    );
                  }
                  return null;
                }}
              />
              <Area
                type="monotone"
                dataKey="count"
                stroke="#10b981"
                strokeWidth={2}
                fillOpacity={1}
                fill="url(#activityGradient)"
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Curriculum Topic Progress Grid */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Layers className="w-4 h-4 text-indigo-400" />
            <h3 className="text-sm font-bold text-white">Curriculum Topics Breakdown</h3>
          </div>
          <span className="text-xs text-slate-500">Click a card to inspect problem status</span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {summary.solvedByTopic.map((t) => (
            <div
              key={t.topic}
              onClick={() => handleTopicClick(t)}
              className="p-4 rounded-xl bg-slate-900 border border-slate-800 hover:border-indigo-500/50 hover:bg-slate-850 transition-all cursor-pointer group space-y-3"
            >
              <div className="flex items-center justify-between">
                <span className="font-bold text-xs text-white group-hover:text-indigo-300 transition-colors">
                  {t.displayName}
                </span>
                <span className="text-xs font-mono font-bold text-indigo-400">
                  {t.completionPercentage}%
                </span>
              </div>

              {/* Progress bar */}
              <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
                <div
                  className="h-full bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full transition-all duration-500"
                  style={{ width: `${t.completionPercentage}%` }}
                />
              </div>

              <div className="flex items-center justify-between text-[11px] text-slate-400">
                <span>{t.solvedCount} solved / {t.attemptedCount} attempted</span>
                <ChevronRight className="w-3.5 h-3.5 text-slate-500 group-hover:translate-x-1 transition-transform" />
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Topic Drill-Down Modal */}
      {selectedTopic && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-fade-in">
          <div className="relative w-full max-w-2xl max-h-[85vh] flex flex-col bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl">
            {/* Modal Header */}
            <div className="p-5 border-b border-slate-800 flex items-center justify-between">
              <div>
                <h3 className="text-base font-bold text-white flex items-center space-x-2">
                  <span>{selectedTopic.displayName} Problems</span>
                  <span className="text-xs font-mono px-2 py-0.5 rounded-full bg-indigo-500/20 text-indigo-300">
                    {selectedTopic.solvedCount} Solved
                  </span>
                </h3>
                <p className="text-xs text-slate-400 mt-0.5">
                  Drill-down review of attempted and solved problems in this module
                </p>
              </div>
              <button
                onClick={() => setSelectedTopic(null)}
                className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Modal Content */}
            <div className="flex-1 overflow-y-auto p-5 space-y-3">
              {loadingTopicDetails ? (
                <div className="text-center py-10 text-xs text-slate-500">Loading topic details...</div>
              ) : topicProblems.length === 0 ? (
                <div className="text-center py-12 text-slate-500">
                  <p className="text-xs">No recorded attempts yet in this topic.</p>
                  <p className="text-[11px] text-slate-600 mt-1">Start practicing problems in the Practice tab!</p>
                </div>
              ) : (
                topicProblems.map((p) => (
                  <div
                    key={p.id}
                    className="flex items-center justify-between p-3.5 rounded-xl bg-slate-800/60 border border-slate-700/60"
                  >
                    <div className="space-y-1">
                      <div className="flex items-center space-x-2">
                        <span className="font-mono text-xs font-semibold text-white">
                          #{p.problemId}
                        </span>
                        <DifficultyBadge difficulty={p.difficulty} />
                      </div>
                      <div className="text-[11px] text-slate-400">
                        {p.attempts} {p.attempts === 1 ? 'attempt' : 'attempts'} &bull;{' '}
                        {Math.floor(p.timeSpentSeconds / 60)}m {p.timeSpentSeconds % 60}s spent
                      </div>
                    </div>
                    <div className="flex items-center space-x-3">
                      <StatusBadge status={p.status} />
                      {p.solvedAt && (
                        <span className="text-[10px] text-slate-500">
                          {new Date(p.solvedAt).toLocaleDateString()}
                        </span>
                      )}
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
