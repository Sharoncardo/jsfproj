import React from 'react';
import { Sparkles } from 'lucide-react';

export const EmptyState = ({
  icon: Icon = Sparkles,
  title = 'No data available',
  description = 'Get started by creating your first item.',
  action = null,
}) => {
  return (
    <div className="flex flex-col items-center justify-center p-8 text-center rounded-2xl border border-dashed border-slate-800 bg-slate-900/40">
      <div className="p-3 bg-indigo-500/10 text-indigo-400 rounded-full mb-3">
        <Icon className="w-6 h-6" />
      </div>
      <h3 className="text-base font-semibold text-slate-200 mb-1">{title}</h3>
      <p className="text-sm text-slate-400 max-w-sm mb-4">{description}</p>
      {action && <div>{action}</div>}
    </div>
  );
};
