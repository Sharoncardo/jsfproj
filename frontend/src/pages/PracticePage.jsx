
import React, { useState, useEffect } from "react";
import {
  CheckCircle2,
  Bookmark as BookmarkIcon,
  StickyNote,
  Sparkles,
  Terminal,
  Check,
  AlertCircle,
  Clock,
} from "lucide-react";

import { progressApi } from "../api/progressApi";
import { bookmarksApi } from "../api/bookmarksApi";
import { NoteModal } from "../components/notes/NoteModal";
import SharonPracticeWorkspace from "../components/practice/SharonPracticeWorkspace";

export const PracticePage = () => {
  const [selectedProblem, setSelectedProblem] = useState(null);

  const [noteModalOpen, setNoteModalOpen] = useState(false);
  const [noteModalType, setNoteModalType] = useState("NOTE");

  const [isBookmarked, setIsBookmarked] = useState(false);
  const [bookmarkInsight, setBookmarkInsight] = useState("");
  const [bookmarkTags, setBookmarkTags] = useState("");
  const [showBookmarkForm, setShowBookmarkForm] = useState(false);

  const [timeSpentSeconds, setTimeSpentSeconds] = useState(300);
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState(null);

  useEffect(() => {
    if (!selectedProblem?.id) return;

    let active = true;

    bookmarksApi
      .checkBookmark(selectedProblem.id)
      .then((res) => {
        if (active && res.success) {
          setIsBookmarked(res.data);
        }
      })
      .catch(() => {
        if (active) setIsBookmarked(false);
      });

    return () => {
      active = false;
    };
  }, [selectedProblem?.id]);

  const handleProblemChange = (problem) => {
    setSelectedProblem({
      ...problem,
      topic: problem.topicCode || problem.topicName || "DSA",
      mnemonicHint:
        "Explore the progressive hints to discover the problem-solving approach.",
    });

    setFeedback(null);
    setShowBookmarkForm(false);
    setBookmarkInsight("");
    setBookmarkTags("");
  };

  const handleOpenNote = (type) => {
    setNoteModalType(type);
    setNoteModalOpen(true);
  };

  const handleRecordAttempt = async (status) => {
    if (!selectedProblem?.id) return;

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
          type: "success",
          message:
            status === "SOLVED"
              ? "🎉 Problem marked as SOLVED! Progress updated."
              : "Progress saved as IN_PROGRESS.",
        });
      }
    } catch (err) {
      setFeedback({
        type: "error",
        message:
          err.response?.data?.message || "Failed to record progress.",
      });
    } finally {
      setSubmitting(false);
    }
  };

  const handleSaveBookmark = async () => {
    if (!selectedProblem?.id) return;

    try {
      await bookmarksApi.createBookmark({
        problemId: selectedProblem.id,
        insight:
          bookmarkInsight ||
          "Explore the hints to discover the problem-solving approach.",
        tags: bookmarkTags || (selectedProblem.topic || "DSA").toLowerCase(),
      });

      setIsBookmarked(true);
      setShowBookmarkForm(false);

      setFeedback({
        type: "success",
        message: "📌 Problem saved to your bookmarks!",
      });
    } catch {
      setFeedback({
        type: "error",
        message: "Failed to save bookmark.",
      });
    }
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
            Build your logic, unlock conceptual hints, and solve DSA problems.
          </p>
        </div>

        <div className="flex items-center space-x-2">
          <button
            onClick={() => handleOpenNote("NOTE")}
            className="flex items-center space-x-1.5 px-3 py-2 rounded-xl text-xs font-semibold bg-amber-500/15 text-amber-300 border border-amber-500/30 hover:bg-amber-500/25 transition-all"
          >
            <StickyNote className="w-3.5 h-3.5" />
            <span>Pin Study Note</span>
          </button>

          <button
            onClick={() => handleOpenNote("MNEMONIC")}
            className="flex items-center space-x-1.5 px-3.5 py-2 rounded-xl text-xs font-bold bg-purple-600 text-white shadow-lg shadow-purple-600/30 hover:bg-purple-500 transition-all"
          >
            <Sparkles className="w-3.5 h-3.5" />
            <span>Add Mnemonic</span>
          </button>
        </div>
      </div>

      {/* Sharon's Original Problem-Solving Workspace */}
      <SharonPracticeWorkspace onProblemChange={handleProblemChange} />

      {/* Bookmark and Progress Actions */}
      {selectedProblem && (
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 space-y-4">

          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <div>
              <h3 className="text-sm font-bold text-white">
                {selectedProblem.title}
              </h3>
              <p className="text-xs text-slate-400 mt-1">
                Save your insight or record your progress for this problem.
              </p>
            </div>

            <button
              onClick={() => setShowBookmarkForm(!showBookmarkForm)}
              className={`flex items-center space-x-2 px-3 py-2 rounded-xl text-xs font-semibold border transition-all ${
                isBookmarked
                  ? "bg-purple-500/15 text-purple-300 border-purple-500/30"
                  : "bg-slate-800 text-slate-300 border-slate-700 hover:text-white"
              }`}
            >
              <BookmarkIcon className="w-4 h-4" />
              <span>{isBookmarked ? "Bookmarked" : "Add Bookmark"}</span>
            </button>
          </div>

          {showBookmarkForm && (
            <div className="p-4 rounded-xl bg-purple-950/20 border border-purple-500/30 space-y-3">
              <textarea
                rows={2}
                value={bookmarkInsight}
                onChange={(e) => setBookmarkInsight(e.target.value)}
                placeholder="Write your personal insight..."
                className="w-full p-3 text-xs bg-slate-900 border border-slate-700 rounded-lg text-white"
              />

              <div className="flex gap-2">
                <input
                  value={bookmarkTags}
                  onChange={(e) => setBookmarkTags(e.target.value)}
                  placeholder="Tags (e.g. arrays, hashmap)"
                  className="flex-1 p-2 text-xs bg-slate-900 border border-slate-700 rounded-lg text-white"
                />

                <button
                  onClick={handleSaveBookmark}
                  className="px-4 py-2 text-xs font-bold text-white bg-purple-600 rounded-lg"
                >
                  Save
                </button>
              </div>
            </div>
          )}

          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pt-2">
            <div className="flex items-center gap-2 text-xs text-slate-400">
              <Clock className="w-4 h-4" />
              <span>Time spent:</span>

              <select
                value={timeSpentSeconds}
                onChange={(e) => setTimeSpentSeconds(Number(e.target.value))}
                className="bg-slate-800 border border-slate-700 rounded-lg px-2 py-1 text-white"
              >
                <option value={120}>2 minutes</option>
                <option value={300}>5 minutes</option>
                <option value={600}>10 minutes</option>
                <option value={1200}>20 minutes</option>
                <option value={1800}>30 minutes</option>
              </select>
            </div>

            <div className="flex gap-2">
              <button
                disabled={submitting}
                onClick={() => handleRecordAttempt("IN_PROGRESS")}
                className="px-4 py-2 rounded-xl text-xs font-semibold bg-slate-800 text-slate-200 border border-slate-700 disabled:opacity-50"
              >
                Mark In Progress
              </button>

              <button
                disabled={submitting}
                onClick={() => handleRecordAttempt("SOLVED")}
                className="flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-bold text-white bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50"
              >
                <CheckCircle2 className="w-4 h-4" />
                Mark Solved
              </button>
            </div>
          </div>

          {feedback && (
            <div
              className={`p-3 rounded-xl text-xs flex items-center gap-2 ${
                feedback.type === "success"
                  ? "bg-emerald-500/10 border border-emerald-500/20 text-emerald-300"
                  : "bg-rose-500/10 border border-rose-500/20 text-rose-300"
              }`}
            >
              {feedback.type === "success" ? (
                <Check className="w-4 h-4" />
              ) : (
                <AlertCircle className="w-4 h-4" />
              )}
              {feedback.message}
            </div>
          )}
        </div>
      )}

      {/* Existing Note Modal */}
      <NoteModal
        isOpen={noteModalOpen}
        onClose={() => setNoteModalOpen(false)}
        defaultProblemId={selectedProblem?.id}
        initialType={noteModalType}
      />
    </div>
  );
};
