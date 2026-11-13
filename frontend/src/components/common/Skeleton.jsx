import React from 'react';

export const Skeleton = ({ className = '' }) => {
  return (
    <div
      className={`animate-pulse bg-slate-800/80 rounded-lg ${className}`}
    />
  );
};

export const DashboardSkeleton = () => {
  return (
    <div className="space-y-6">
      {/* Metric Cards Skeleton */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {[1, 2, 3, 4].map((i) => (
          <div key={i} className="p-5 rounded-2xl bg-slate-900 border border-slate-800">
            <Skeleton className="h-4 w-24 mb-3" />
            <Skeleton className="h-8 w-16 mb-2" />
            <Skeleton className="h-3 w-32" />
          </div>
        ))}
      </div>

      {/* Charts Skeleton */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 p-6 rounded-2xl bg-slate-900 border border-slate-800">
          <Skeleton className="h-6 w-48 mb-6" />
          <Skeleton className="h-64 w-full" />
        </div>
        <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800">
          <Skeleton className="h-6 w-36 mb-6" />
          <Skeleton className="h-64 w-full rounded-full" />
        </div>
      </div>
    </div>
  );
};
