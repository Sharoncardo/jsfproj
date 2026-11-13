import React, { useState, useRef, useEffect } from 'react';
import { Pin, X, Minimize2, Maximize2, Sparkles, BookOpen, Move } from 'lucide-react';
import { useNotes } from '../../context/NotesContext';

const COLOR_OPTIONS = [
  { name: 'Yellow', hex: '#FEF08A', text: '#713f12' },
  { name: 'Blue', hex: '#BAE6FD', text: '#0c4a6e' },
  { name: 'Green', hex: '#BBF7D0', text: '#14532d' },
  { name: 'Pink', hex: '#FBCFE8', text: '#831843' },
  { name: 'Purple', hex: '#E9D5FF', text: '#581c87' },
  { name: 'Amber', hex: '#FED7AA', text: '#7c2d12' },
];

export const DraggableNote = ({ note }) => {
  const { updateNote, updateNotePosition, deleteNote, togglePin } = useNotes();
  const [isMinimized, setIsMinimized] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState(note.title);
  const [content, setContent] = useState(note.content);
  const [color, setColor] = useState(note.color || '#FEF08A');
  const [showColorPicker, setShowColorPicker] = useState(false);

  // Dragging state
  const [isDragging, setIsDragging] = useState(false);
  const dragStartPos = useRef({ x: 0, y: 0 });
  const initialNotePos = useRef({ x: note.positionX, y: note.positionY });

  const isMnemonic = note.type === 'MNEMONIC';

  // Handle Dragging
  const handleMouseDown = (e) => {
    // Only drag from header handle
    if (e.target.closest('button') || e.target.closest('input') || e.target.closest('textarea')) {
      return;
    }
    setIsDragging(true);
    dragStartPos.current = { x: e.clientX, y: e.clientY };
    initialNotePos.current = { x: note.positionX, y: note.positionY };
    e.preventDefault();
  };

  useEffect(() => {
    const handleMouseMove = (e) => {
      if (!isDragging) return;
      const deltaX = e.clientX - dragStartPos.current.x;
      const deltaY = e.clientY - dragStartPos.current.y;

      const newX = Math.max(10, Math.min(window.innerWidth - 300, initialNotePos.current.x + deltaX));
      const newY = Math.max(10, Math.min(window.innerHeight - 150, initialNotePos.current.y + deltaY));

      updateNotePosition(note.id, {
        positionX: newX,
        positionY: newY,
        width: note.width,
        height: note.height,
      });
    };

    const handleMouseUp = () => {
      if (isDragging) {
        setIsDragging(false);
      }
    };

    if (isDragging) {
      window.addEventListener('mousemove', handleMouseMove);
      window.addEventListener('mouseup', handleMouseUp);
    }

    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
      window.removeEventListener('mouseup', handleMouseUp);
    };
  }, [isDragging, note.id, note.width, note.height, updateNotePosition]);

  const handleBlurSave = () => {
    setIsEditing(false);
    if (title !== note.title || content !== note.content) {
      updateNote(note.id, { title, content });
    }
  };

  const handleSelectColor = (newColor) => {
    setColor(newColor);
    setShowColorPicker(false);
    updateNote(note.id, { color: newColor });
  };

  return (
    <div
      style={{
        transform: `translate3d(${note.positionX}px, ${note.positionY}px, 0)`,
        width: isMinimized ? '260px' : `${note.width || 280}px`,
        backgroundColor: color,
        zIndex: note.pinned ? 45 : 40,
      }}
      className={`fixed top-0 left-0 rounded-xl transition-shadow select-none ${
        isMnemonic ? 'mnemonic-glow border-2 border-indigo-400/80 ring-2 ring-indigo-500/20' : 'sticky-note-shadow border border-black/10'
      }`}
    >
      {/* Header / Drag Bar */}
      <div
        onMouseDown={handleMouseDown}
        className="flex items-center justify-between px-3 py-2 cursor-grab active:cursor-grabbing border-b border-black/10 rounded-t-xl bg-black/5"
      >
        <div className="flex items-center space-x-1.5 overflow-hidden">
          <Move className="w-3.5 h-3.5 text-slate-700/70 flex-shrink-0" />
          {isMnemonic ? (
            <span className="inline-flex items-center space-x-1 px-1.5 py-0.5 rounded text-[10px] font-bold bg-indigo-600 text-white shadow-sm">
              <Sparkles className="w-2.5 h-2.5" />
              <span>MNEMONIC</span>
            </span>
          ) : (
            <span className="inline-flex items-center space-x-1 px-1.5 py-0.5 rounded text-[10px] font-semibold bg-black/15 text-slate-800">
              <BookOpen className="w-2.5 h-2.5" />
              <span>NOTE</span>
            </span>
          )}
          {note.problemId && (
            <span className="text-[10px] font-medium text-slate-700 truncate max-w-[90px]">
              #{note.problemId}
            </span>
          )}
        </div>

        {/* Action icons */}
        <div className="flex items-center space-x-1 text-slate-700">
          {/* Color picker toggle */}
          <div className="relative">
            <button
              onClick={() => setShowColorPicker(!showColorPicker)}
              title="Change Color"
              className="w-4 h-4 rounded-full border border-black/20 hover:scale-110 transition-transform"
              style={{ backgroundColor: color }}
            />
            {showColorPicker && (
              <div className="absolute right-0 top-6 z-50 p-1.5 bg-slate-900 border border-slate-700 rounded-lg shadow-xl flex space-x-1">
                {COLOR_OPTIONS.map((c) => (
                  <button
                    key={c.name}
                    onClick={() => handleSelectColor(c.hex)}
                    style={{ backgroundColor: c.hex }}
                    className="w-5 h-5 rounded-full border border-white/20 hover:scale-110 transition-transform"
                    title={c.name}
                  />
                ))}
              </div>
            )}
          </div>

          {/* Pin toggle */}
          <button
            onClick={() => togglePin(note.id)}
            title={note.pinned ? 'Unpin' : 'Pin on screen'}
            className={`p-1 rounded hover:bg-black/10 transition-colors ${
              note.pinned ? 'text-indigo-700 font-bold' : 'text-slate-600'
            }`}
          >
            <Pin className={`w-3.5 h-3.5 ${note.pinned ? 'fill-indigo-600' : ''}`} />
          </button>

          {/* Minimize / Maximize */}
          <button
            onClick={() => setIsMinimized(!isMinimized)}
            title={isMinimized ? 'Expand' : 'Minimize'}
            className="p-1 rounded hover:bg-black/10 text-slate-600 transition-colors"
          >
            {isMinimized ? <Maximize2 className="w-3.5 h-3.5" /> : <Minimize2 className="w-3.5 h-3.5" />}
          </button>

          {/* Delete */}
          <button
            onClick={() => deleteNote(note.id)}
            title="Delete Note"
            className="p-1 rounded hover:bg-rose-500 hover:text-white text-slate-600 transition-colors"
          >
            <X className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* Body */}
      {!isMinimized && (
        <div className="p-3 text-slate-900">
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            onBlur={handleBlurSave}
            placeholder="Note title..."
            className="w-full bg-transparent font-bold text-sm text-slate-900 placeholder:text-slate-600/60 focus:outline-none mb-1.5 border-b border-transparent focus:border-black/20"
          />
          <textarea
            rows={5}
            value={content}
            onChange={(e) => setContent(e.target.value)}
            onBlur={handleBlurSave}
            placeholder="Write DSA insight, trick, or formula..."
            className="w-full bg-transparent text-xs text-slate-800 placeholder:text-slate-600/60 resize-none focus:outline-none leading-relaxed"
          />
          <div className="flex items-center justify-between pt-1 border-t border-black/5 text-[10px] text-slate-600">
            <span>{isMnemonic ? '🧠 Quick Mnemonic' : '📝 Study Note'}</span>
            <span>{new Date(note.updatedAt || note.createdAt).toLocaleDateString()}</span>
          </div>
        </div>
      )}

      {/* Minimized label */}
      {isMinimized && (
        <div
          onClick={() => setIsMinimized(false)}
          className="px-3 py-1.5 text-xs font-semibold text-slate-800 truncate cursor-pointer hover:bg-black/5"
        >
          {title || 'Untitled Note'}
        </div>
      )}
    </div>
  );
};
