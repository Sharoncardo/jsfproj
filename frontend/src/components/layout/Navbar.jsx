import React, { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  Code2,
  LayoutDashboard,
  Terminal,
  User,
  LogOut,
  Bookmark,
  StickyNote,
  Sparkles,
  ChevronDown
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useNotes } from '../../context/NotesContext';
import { BookmarksDrawer } from '../bookmarks/BookmarksDrawer';

export const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const { notes, overlayVisible, setOverlayVisible } = useNotes();
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [bookmarksOpen, setBookmarksOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navLinks = [
    { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
    { name: 'Practice', path: '/practice', icon: Terminal },
    { name: 'Profile', path: '/profile', icon: User },
  ];

  return (
    <>
      <nav className="sticky top-0 z-30 bg-slate-950/85 backdrop-blur-md border-b border-slate-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            {/* Logo */}
            <div className="flex items-center space-x-3">
              <Link to="/dashboard" className="flex items-center space-x-2.5 group">
                <div className="p-2 rounded-xl bg-gradient-to-tr from-indigo-600 to-purple-600 text-white shadow-lg shadow-indigo-500/25 group-hover:scale-105 transition-transform">
                  <Code2 className="w-5 h-5" />
                </div>
                <span className="text-lg font-extrabold tracking-tight bg-gradient-to-r from-white via-slate-200 to-indigo-300 bg-clip-text text-transparent">
                  Think2Code
                </span>
              </Link>

              {/* Navigation Links */}
              {isAuthenticated && (
                <div className="hidden md:flex items-center space-x-1 ml-6">
                  {navLinks.map((link) => {
                    const Icon = link.icon;
                    const isActive = location.pathname === link.path;
                    return (
                      <Link
                        key={link.path}
                        to={link.path}
                        className={`flex items-center space-x-1.5 px-3 py-1.5 rounded-xl text-xs font-semibold transition-all ${
                          isActive
                            ? 'bg-indigo-600/15 text-indigo-400 border border-indigo-500/30'
                            : 'text-slate-400 hover:text-white hover:bg-slate-900'
                        }`}
                      >
                        <Icon className="w-3.5 h-3.5" />
                        <span>{link.name}</span>
                      </Link>
                    );
                  })}
                </div>
              )}
            </div>

            {/* Right actions */}
            <div className="flex items-center space-x-2 sm:space-x-3">
              {isAuthenticated ? (
                <>
                  {/* Bookmarks drawer trigger */}
                  <button
                    onClick={() => setBookmarksOpen(true)}
                    title="View Bookmarks"
                    className="flex items-center space-x-1 px-3 py-1.5 rounded-xl text-xs font-semibold text-slate-300 hover:text-white bg-slate-900 hover:bg-slate-800 border border-slate-800 transition-colors"
                  >
                    <Bookmark className="w-3.5 h-3.5 text-indigo-400" />
                    <span className="hidden sm:inline">Bookmarks</span>
                  </button>

                  {/* Notes overlay quick toggle */}
                  <button
                    onClick={() => setOverlayVisible(!overlayVisible)}
                    title="Toggle Floating Sticky Notes"
                    className={`flex items-center space-x-1 px-3 py-1.5 rounded-xl text-xs font-semibold border transition-all ${
                      overlayVisible
                        ? 'bg-amber-500/10 text-amber-300 border-amber-500/30'
                        : 'bg-slate-900 text-slate-400 border-slate-800 hover:text-slate-200'
                    }`}
                  >
                    <StickyNote className="w-3.5 h-3.5" />
                    <span className="hidden sm:inline">Notes</span>
                    <span className="ml-1 px-1.5 py-0.2 rounded-full text-[10px] bg-slate-800 text-slate-300 font-mono">
                      {notes.length}
                    </span>
                  </button>

                  {/* User Profile dropdown */}
                  <div className="relative">
                    <button
                      onClick={() => setDropdownOpen(!dropdownOpen)}
                      className="flex items-center space-x-2 p-1.5 rounded-xl hover:bg-slate-900 transition-colors"
                    >
                      {user?.avatarUrl ? (
                        <img
                          src={user.avatarUrl}
                          alt={user.displayName || user.username}
                          className="w-7 h-7 rounded-full object-cover border border-indigo-500/40"
                        />
                      ) : (
                        <div className="w-7 h-7 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-600 flex items-center justify-center text-xs font-bold text-white uppercase">
                          {user?.username?.[0] || 'U'}
                        </div>
                      )}
                      <span className="hidden sm:inline text-xs font-semibold text-slate-200">
                        {user?.displayName || user?.username}
                      </span>
                      <ChevronDown className="w-3 h-3 text-slate-400" />
                    </button>

                    {dropdownOpen && (
                      <div className="absolute right-0 mt-2 w-48 py-1.5 rounded-xl bg-slate-900 border border-slate-800 shadow-2xl z-50 animate-fade-in">
                        <div className="px-3 py-2 border-b border-slate-800">
                          <p className="text-xs font-bold text-white truncate">{user?.displayName || user?.username}</p>
                          <p className="text-[10px] text-slate-400 truncate">{user?.email}</p>
                          <span className="inline-block mt-1 px-1.5 py-0.5 rounded text-[10px] font-bold bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                            {user?.role || 'USER'}
                          </span>
                        </div>
                        <Link
                          to="/profile"
                          onClick={() => setDropdownOpen(false)}
                          className="flex items-center space-x-2 px-3 py-2 text-xs text-slate-300 hover:text-white hover:bg-slate-800/80 transition-colors"
                        >
                          <User className="w-3.5 h-3.5" />
                          <span>Profile & Stats</span>
                        </Link>
                        <button
                          onClick={() => {
                            setDropdownOpen(false);
                            handleLogout();
                          }}
                          className="w-full flex items-center space-x-2 px-3 py-2 text-xs text-rose-400 hover:bg-rose-500/10 transition-colors"
                        >
                          <LogOut className="w-3.5 h-3.5" />
                          <span>Sign Out</span>
                        </button>
                      </div>
                    )}
                  </div>
                </>
              ) : (
                <div className="flex items-center space-x-2">
                  <Link
                    to="/login"
                    className="px-3.5 py-1.5 text-xs font-semibold text-slate-300 hover:text-white"
                  >
                    Log In
                  </Link>
                  <Link
                    to="/register"
                    className="px-4 py-1.5 rounded-xl text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 shadow-md shadow-indigo-600/30 transition-all"
                  >
                    Get Started
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      <BookmarksDrawer isOpen={bookmarksOpen} onClose={() => setBookmarksOpen(false)} />
    </>
  );
};
