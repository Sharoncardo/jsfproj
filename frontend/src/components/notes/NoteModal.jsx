import React, { useState } from 'react';
import { X, Sparkles, BookOpen } from 'lucide-react';
import { useNotes } from '../../context/NotesContext';

const COLORS = [
  { name: 'Yellow', hex: '#FEF08A' },
  { name: 'Blue', hex: '#BAE6FD' },
  { name: 'Green', hex: '#BBF7D0' },
  { name: 'Pink', hex: '#FBCFE8' },
  { name: 'Purple', hex: '#E9D5FF' },
  { name: 'Amber', hex: '#FED7AA' },
];

export const NoteModal = ({ isOpen, onClose, defaultProblemId = '', initialType = 'NOTE' }) => {
  const { addNote } = useNotes();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [type, setType] = useState(initialType);
  const [color, setColor] = useState(initialType === 'MNEMONIC' ? '#E9D5FF' : '#FEF08A');
  const [problemId, setProblemId] = useState(defaultProblemId);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title.trim() || !content.trim()) {
      setError('Title and content are required');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      // Spawn note near the center of the viewport
      const posX = Math.max(80, Math.floor(window.innerWidth / 2 - 140 + (Math.random() * 80 - 40)));
      const posY = Math.max(120, Math.floor(window.innerHeight / 2 - 120 + (Math.random() * 60 - 30)));

      await addNote({
        title: title.trim(),
        content: content.trim(),
        type,
        color,
        problemId: problemId.trim() || null,
        positionX: posX,
        positionY: posY,
        width: 280,
        height: 240,
        pinned: false,
      });

      onClose();
      setTitle('');
      setContent('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create note');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-md p-6 bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-slate-400 hover:text-white p-1 rounded-lg hover:bg-slate-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        <h3 className="text-lg font-bold text-white mb-1">
          {type === 'MNEMONIC' ? 'Create Algorithm Mnemonic' : 'Create Sticky Note'}
        </h3>
        <p className="text-xs text-slate-400 mb-4">
          Personal reminders and memory triggers attached to your DSA workspace.
        </p>

        {error && (
          <div className="p-3 mb-4 text-xs text-rose-300 bg-rose-500/10 border border-rose-500/20 rounded-xl">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Note Type Selector */}
          <div className="grid grid-cols-2 gap-2">
            <button
              type="button"
              onClick={() => {
                setType('NOTE');
                if (color === '#E9D5FF') setColor('#FEF08A');
              }}
              className={`flex items-center justify-center space-x-2 p-2.5 rounded-xl border text-xs font-semibold transition-all ${
                type === 'NOTE'
                  ? 'bg-indigo-600/20 border-indigo-500 text-indigo-300'
                  : 'bg-slate-800/60 border-slate-700 text-slate-400 hover:text-slate-200'
              }`}
            >
              <BookOpen className="w-4 h-4" />
              <span>Standard Note</span>
            </button>
            <button
              type="button"
              onClick={() => {
                setType('MNEMONIC');
                setColor('#E9D5FF');
              }}
              className={`flex items-center justify-center space-x-2 p-2.5 rounded-xl border text-xs font-semibold transition-all ${
                type === 'MNEMONIC'
                  ? 'bg-purple-600/30 border-purple-500 text-purple-300 ring-2 ring-purple-500/20'
                  : 'bg-slate-800/60 border-slate-700 text-slate-400 hover:text-slate-200'
              }`}
            >
              <Sparkles className="w-4 h-4 text-purple-400" />
              <span>Mnemonic / Rule</span>
            </button>
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Title</label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="e.g. Floyd's Cycle Rule or Two-Sum Hashmap Trick"
              className="w-full px-3 py-2 text-sm bg-slate-800 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              required
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Content</label>
            <textarea
              rows={4}
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="Summarize the core mental model, intuition, or code pattern..."
              className="w-full px-3 py-2 text-sm bg-slate-800 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500 resize-none"
              required
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Attach to Problem ID (optional)
              </label>
              <input
                type="text"
                value={problemId}
                onChange={(e) => setProblemId(e.target.value)}
                placeholder="e.g. two-sum"
                className="w-full px-3 py-2 text-xs bg-slate-800 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">Color Palette</label>
              <div className="flex items-center space-x-1.5 py-1">
                {COLORS.map((c) => (
                  <button
                    key={c.name}
                    type="button"
                    onClick={() => setColor(c.hex)}
                    style={{ backgroundColor: c.hex }}
                    className={`w-6 h-6 rounded-full transition-transform ${
                      color === c.hex ? 'scale-125 ring-2 ring-white' : 'hover:scale-110'
                    }`}
                  />
                ))}
              </div>
            </div>
          </div>

          <div className="flex items-center justify-end space-x-3 pt-3 border-t border-slate-800">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-xs font-semibold text-slate-400 hover:text-white"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 rounded-xl shadow-lg shadow-indigo-600/30 transition-all disabled:opacity-50"
            >
              {submitting ? 'Creating...' : 'Create Note'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
