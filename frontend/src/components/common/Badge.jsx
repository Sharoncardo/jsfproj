import React from 'react';

export const DifficultyBadge = ({ difficulty }) => {
  const styles = {
    EASY: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
    MEDIUM: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
    HARD: 'bg-rose-500/10 text-rose-400 border-rose-500/20',
  };

  return (
    <span
      className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-semibold border ${
        styles[difficulty] || 'bg-slate-700 text-slate-300 border-slate-600'
      }`}
    >
      {difficulty}
    </span>
  );
};

export const StatusBadge = ({ status }) => {
  const styles = {
    SOLVED: 'bg-emerald-500/15 text-emerald-400 border-emerald-500/30',
    IN_PROGRESS: 'bg-blue-500/15 text-blue-400 border-blue-500/30',
    NOT_STARTED: 'bg-slate-800 text-slate-400 border-slate-700',
  };

  const labels = {
    SOLVED: 'Solved',
    IN_PROGRESS: 'In Progress',
    NOT_STARTED: 'Todo',
  };

  return (
    <span
      className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium border ${
        styles[status] || styles.NOT_STARTED
      }`}
    >
      <span
        className={`w-1.5 h-1.5 rounded-full mr-1.5 ${
          status === 'SOLVED'
            ? 'bg-emerald-400'
            : status === 'IN_PROGRESS'
            ? 'bg-blue-400'
            : 'bg-slate-500'
        }`}
      />
      {labels[status] || status}
    </span>
  );
};

export const TopicBadge = ({ topic, displayName }) => {
  return (
    <span className="inline-flex items-center px-2 py-0.5 rounded bg-indigo-500/10 text-indigo-300 border border-indigo-500/20 text-xs font-medium">
      {displayName || topic}
    </span>
  );
};
