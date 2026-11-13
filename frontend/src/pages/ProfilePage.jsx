import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import {
  User as UserIcon,
  Mail,
  Calendar,
  Shield,
  Edit3,
  Bookmark,
  Check,
  AlertCircle,
  ExternalLink,
  Trash2,
  Sparkles,
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { bookmarksApi } from '../api/bookmarksApi';
import { progressApi } from '../api/progressApi';
import { Link } from 'react-router-dom';

export const ProfilePage = () => {
  const { user, updateUser } = useAuth();
  const [bookmarks, setBookmarks] = useState([]);
  const [summary, setSummary] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const {
    register,
    handleSubmit,
    reset,
    formState: { isSubmitting },
  } = useForm({
    defaultValues: {
      displayName: user?.displayName || '',
      avatarUrl: user?.avatarUrl || '',
    },
  });

  useEffect(() => {
    if (user) {
      reset({
        displayName: user.displayName || '',
        avatarUrl: user.avatarUrl || '',
      });
    }

    bookmarksApi
      .getBookmarks(0, 10)
      .then((res) => {
        if (res.success && res.data) {
          setBookmarks(res.data.content || []);
        }
      })
      .catch(console.error);

    progressApi
      .getSummary()
      .then((res) => {
        if (res.success) {
          setSummary(res.data);
        }
      })
      .catch(console.error);
  }, [user, reset]);

  const onUpdateProfile = async (data) => {
    setSuccessMessage('');
    setErrorMessage('');
    try {
      await updateUser({
        displayName: data.displayName,
        avatarUrl: data.avatarUrl || null,
      });
      setSuccessMessage('Profile updated successfully!');
      setIsEditing(false);
    } catch (err) {
      setErrorMessage(err.response?.data?.message || 'Failed to update profile');
    }
  };

  const handleDeleteBookmark = async (id) => {
    try {
      await bookmarksApi.deleteBookmark(id);
      setBookmarks((prev) => prev.filter((b) => b.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8 animate-fade-in">
      {/* Profile Header Card */}
      <div className="p-6 sm:p-8 rounded-2xl bg-slate-900 border border-slate-800 shadow-xl relative overflow-hidden">
        <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6 relative z-10">
          {/* Avatar */}
          <div className="relative group">
            {user?.avatarUrl ? (
              <img
                src={user.avatarUrl}
                alt={user.displayName || user.username}
                className="w-24 h-24 rounded-2xl object-cover border-2 border-indigo-500/40 shadow-xl"
              />
            ) : (
              <div className="w-24 h-24 rounded-2xl bg-gradient-to-tr from-indigo-600 to-purple-600 flex items-center justify-center text-3xl font-extrabold text-white shadow-xl">
                {user?.username?.[0]?.toUpperCase() || 'U'}
              </div>
            )}
            <span className="absolute -bottom-2 -right-2 px-2 py-0.5 rounded-full text-[10px] font-bold bg-indigo-600 text-white shadow-md border border-slate-900">
              {user?.role || 'USER'}
            </span>
          </div>

          {/* User Info */}
          <div className="flex-1 text-center sm:text-left space-y-2">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
              <div>
                <h1 className="text-2xl font-extrabold text-white tracking-tight">
                  {user?.displayName || user?.username}
                </h1>
                <p className="text-xs font-mono text-indigo-400">@{user?.username}</p>
              </div>

              <button
                onClick={() => setIsEditing(!isEditing)}
                className="inline-flex items-center space-x-1.5 px-3 py-1.5 rounded-xl text-xs font-semibold bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 transition-colors self-center sm:self-start"
              >
                <Edit3 className="w-3.5 h-3.5" />
                <span>{isEditing ? 'Cancel Edit' : 'Edit Profile'}</span>
              </button>
            </div>

            <div className="flex flex-wrap items-center justify-center sm:justify-start gap-4 text-xs text-slate-400 pt-1">
              <span className="flex items-center space-x-1.5">
                <Mail className="w-3.5 h-3.5 text-slate-500" />
                <span>{user?.email}</span>
              </span>
              <span className="flex items-center space-x-1.5">
                <Calendar className="w-3.5 h-3.5 text-slate-500" />
                <span>
                  Member since {user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'recently'}
                </span>
              </span>
            </div>
          </div>
        </div>

        {/* Edit Form if toggled */}
        {isEditing && (
          <form
            onSubmit={handleSubmit(onUpdateProfile)}
            className="mt-6 pt-6 border-t border-slate-800 space-y-4 max-w-lg"
          >
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Display Name</label>
              <input
                type="text"
                {...register('displayName')}
                placeholder="Your name"
                className="w-full px-3 py-2 text-xs bg-slate-800 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Avatar Image URL</label>
              <input
                type="url"
                {...register('avatarUrl')}
                placeholder="https://images.unsplash.com/..."
                className="w-full px-3 py-2 text-xs bg-slate-800 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              />
            </div>
            <button
              type="submit"
              disabled={isSubmitting}
              className="px-4 py-2 text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 rounded-xl shadow-md transition-all disabled:opacity-50"
            >
              {isSubmitting ? 'Saving...' : 'Save Profile Changes'}
            </button>
          </form>
        )}

        {successMessage && (
          <div className="mt-4 p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-300 text-xs flex items-center space-x-2">
            <Check className="w-4 h-4" />
            <span>{successMessage}</span>
          </div>
        )}
        {errorMessage && (
          <div className="mt-4 p-3 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-300 text-xs flex items-center space-x-2">
            <AlertCircle className="w-4 h-4" />
            <span>{errorMessage}</span>
          </div>
        )}
      </div>

      {/* Stats Snapshot */}
      {summary && (
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
          <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 text-center">
            <p className="text-xs text-slate-400">Total Solved</p>
            <p className="text-2xl font-extrabold text-white font-mono mt-1">{summary.totalSolved}</p>
          </div>
          <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 text-center">
            <p className="text-xs text-slate-400">Active Streak</p>
            <p className="text-2xl font-extrabold text-amber-400 font-mono mt-1">
              {summary.currentStreakDays} days
            </p>
          </div>
          <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 text-center">
            <p className="text-xs text-slate-400">Longest Streak</p>
            <p className="text-2xl font-extrabold text-indigo-400 font-mono mt-1">
              {summary.longestStreakDays} days
            </p>
          </div>
          <div className="p-4 rounded-xl bg-slate-900 border border-slate-800 text-center">
            <p className="text-xs text-slate-400">Curriculum Mastery</p>
            <p className="text-2xl font-extrabold text-emerald-400 font-mono mt-1">
              {summary.overallCompletionPercentage}%
            </p>
          </div>
        </div>
      )}

      {/* Saved Bookmarks Preview */}
      <div className="p-6 rounded-2xl bg-slate-900 border border-slate-800 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Bookmark className="w-4 h-4 text-purple-400" />
            <h3 className="text-sm font-bold text-white">Your Problem Insights & Bookmarks</h3>
          </div>
          <span className="text-xs text-slate-400">{bookmarks.length} saved</span>
        </div>

        {bookmarks.length === 0 ? (
          <p className="text-xs text-slate-500 py-6 text-center">
            No bookmarks saved yet. Star insights in the Practice section!
          </p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            {bookmarks.map((b) => (
              <div
                key={b.id}
                className="p-3.5 rounded-xl bg-slate-800/60 border border-slate-700/60 flex flex-col justify-between space-y-2"
              >
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs font-bold text-indigo-300">
                    #{b.problemId}
                  </span>
                  <div className="flex items-center space-x-1">
                    <Link
                      to={`/practice?problem=${b.problemId}`}
                      className="p-1 text-slate-400 hover:text-white"
                      title="Open problem"
                    >
                      <ExternalLink className="w-3.5 h-3.5" />
                    </Link>
                    <button
                      onClick={() => handleDeleteBookmark(b.id)}
                      className="p-1 text-slate-400 hover:text-rose-400"
                      title="Delete bookmark"
                    >
                      <Trash2 className="w-3.5 h-3.5" />
                    </button>
                  </div>
                </div>
                <p className="text-xs text-slate-300 leading-relaxed font-sans">{b.insight}</p>
                {b.tags && (
                  <div className="flex flex-wrap gap-1 text-[10px] text-slate-400">
                    {b.tags.split(',').map((t, i) => (
                      <span key={i} className="px-1.5 py-0.5 rounded bg-slate-700/60 text-slate-300">
                        {t.trim()}
                      </span>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
