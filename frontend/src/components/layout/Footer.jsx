import React from 'react';
import { Code2, Heart } from 'lucide-react';

export const Footer = () => {
  return (
    <footer className="mt-auto border-t border-slate-900 bg-slate-950/60 py-6 text-center text-xs text-slate-500">
      <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-3">
        <div className="flex items-center space-x-2">
          <Code2 className="w-4 h-4 text-indigo-500" />
          <span className="font-semibold text-slate-400">Think2Code Platform</span>
          <span>&copy; {new Date().getFullYear()}</span>
        </div>
        <p className="flex items-center space-x-1">
          <span>User Auth, Progress & Personalization Module</span>
        </p>
      </div>
    </footer>
  );
};
