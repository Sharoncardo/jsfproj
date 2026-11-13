import React, { useState, useEffect, useCallback } from 'react';
import { X, Bookmark, Trash2, Edit2, Check, ExternalLink, Search, Tag } from 'lucide-react';
import { bookmarksApi } from '../../api/bookmarksApi';
import { useNavigate } from 'react-router-dom';

export const BookmarksDrawer = ({ isOpen, onClose }) => {
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editInsight, setEditInsight] = useState('');
  const [editTags, setEditTags] = useState('');
  const navigate = useNavigate();

  const fetchBookmarks = useCallback(async () => {
    setLoading(true);
    try {
      const res = await bookmarksApi.getBookmarks(0, 50);
      if (res.success && res.data) {
        setBookmarks(res.data.content || []);
      }
    } catch (err) {
      console.error('Failed to load bookmarks:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (isOpen) {
      fetchBookmarks();
    }
  }, [isOpen, fetchBookmarks]);

  const handleDelete = async (id) => {
    try {
      await bookmarksApi.deleteBookmark(id);
      setBookmarks((prev) => prev.filter((b) => b.id !== id));
    } catch (err) {
      console.error('Failed to delete bookmark:', err);
    }
  };

  const handleStartEdit = (b) => {
    setEditingId(b.id);
    setEditInsight(b.insight || '');
    setEditTags(b.tags || '');
  };

  const handleSaveEdit = async (id) => {
    try {
      const res = await bookmarksApi.updateBookmark(id, {
        insight: editInsight,
        tags: editTags,
      });
      if (res.success && res.data) {
        setBookmarks((prev) => prev.map((b) => (b.id === id ? res.data : b)));
        setEditingId(null);
      }
    } catch (err) {
      console.error('Failed to update bookmark:', err);
    }
  };

  if (!isOpen) return null;

  const filtered = bookmarks.filter((b) => {
    const q = search.toLowerCase();
    return (
      b.problemId.toLowerCase().includes(q) ||
      (b.insight && b.insight.toLowerCase().includes(q)) ||
      (b.tags && b.tags.toLowerCase().includes(q))
    );
  });

  return (
    <div className="fixed inset-0 z-50 overflow-hidden bg-slate-950/70 backdrop-blur-sm">
      <div className="absolute inset-y-0 right-0 max-w-full flex pl-10">
        <div className="w-screen max-w-md bg-slate-900 border-l border-slate-800 flex flex-col shadow-2xl">
          {/* Drawer Header */}
          <div className="p-5 border-b border-slate-800 flex items-center justify-between">
            <div className="flex items-center space-x-2.5">
              <div className="p-2 bg-indigo-500/10 text-indigo-400 rounded-xl">
                <Bookmark className="w-5 h-5 fill-indigo-500/20" />
              </div>
              <div>
                <h2 className="text-base font-bold text-white">Problem Bookmarks</h2>
                <p className="text-xs text-slate-400">{bookmarks.length} saved insights</p>
              </div>
            </div>
            <button
              onClick={onClose}
              className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Search bar */}
          <div className="p-4 border-b border-slate-800 bg-slate-900/50">
            <div className="relative">
              <Search className="absolute left-3 top-2.5 w-4 h-4 text-slate-500" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Search by problem, insight, or tag..."
                className="w-full pl-9 pr-3 py-2 text-xs bg-slate-800/80 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              />
            </div>
          </div>

          {/* Bookmarks List */}
          <div className="flex-1 overflow-y-auto p-4 space-y-3">
            {loading ? (
              <div className="text-center py-10 text-xs text-slate-500">Loading bookmarks...</div>
            ) : filtered.length === 0 ? (
              <div className="text-center py-12 text-slate-500">
                <Bookmark className="w-8 h-8 mx-auto mb-2 opacity-30" />
                <p className="text-xs">No bookmarks found</p>
              </div>
            ) : (
              filtered.map((b) => (
                <div
                  key={b.id}
                  className="p-4 rounded-xl bg-slate-800/60 border border-slate-700/60 hover:border-slate-600 transition-all space-y-2.5"
                >
                  <div className="flex items-center justify-between">
                    <span className="font-semibold text-xs text-indigo-300 font-mono">
                      #{b.problemId}
                    </span>
                    <div className="flex items-center space-x-1">
                      <button
                        onClick={() => {
                          onClose();
                          navigate(`/practice?problem=${b.problemId}`);
                        }}
                        title="Practice problem"
                        className="p-1 text-slate-400 hover:text-indigo-400 transition-colors"
                      >
                        <ExternalLink className="w-3.5 h-3.5" />
                      </button>
                      {editingId === b.id ? (
                        <button
                          onClick={() => handleSaveEdit(b.id)}
                          title="Save changes"
                          className="p-1 text-emerald-400 hover:text-emerald-300"
                        >
                          <Check className="w-3.5 h-3.5" />
                        </button>
                      ) : (
                        <button
                          onClick={() => handleStartEdit(b)}
                          title="Edit insight"
                          className="p-1 text-slate-400 hover:text-white"
                        >
                          <Edit2 className="w-3.5 h-3.5" />
                        </button>
                      )}
                      <button
                        onClick={() => handleDelete(b.id)}
                        title="Delete bookmark"
                        className="p-1 text-slate-400 hover:text-rose-400"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  </div>

                  {editingId === b.id ? (
                    <div className="space-y-2">
                      <textarea
                        rows={3}
                        value={editInsight}
                        onChange={(e) => setEditInsight(e.target.value)}
                        placeholder="Key insight..."
                        className="w-full p-2 text-xs bg-slate-900 border border-slate-700 rounded-lg text-white focus:outline-none focus:border-indigo-500"
                      />
                      <input
                        type="text"
                        value={editTags}
                        onChange={(e) => setEditTags(e.target.value)}
                        placeholder="Tags comma-separated (e.g. dp, greedy)"
                        className="w-full p-2 text-xs bg-slate-900 border border-slate-700 rounded-lg text-white focus:outline-none focus:border-indigo-500"
                      />
                    </div>
                  ) : (
                    <>
                      <p className="text-xs text-slate-300 leading-relaxed font-sans">
                        {b.insight || 'No notes added for this bookmark.'}
                      </p>
                      {b.tags && (
                        <div className="flex flex-wrap gap-1 pt-1">
                          {b.tags.split(',').map((tag, i) => (
                            <span
                              key={i}
                              className="inline-flex items-center space-x-1 px-1.5 py-0.5 rounded text-[10px] bg-indigo-500/10 text-indigo-300 border border-indigo-500/20"
                            >
                              <Tag className="w-2.5 h-2.5 opacity-60" />
                              <span>{tag.trim()}</span>
                            </span>
                          ))}
                        </div>
                      )}
                    </>
                  )}

                  <div className="text-[10px] text-slate-500 pt-1 border-t border-slate-700/40">
                    Saved {new Date(b.createdAt).toLocaleDateString()}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
