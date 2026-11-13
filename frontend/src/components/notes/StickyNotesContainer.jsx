import React, { useState } from 'react';
import { Plus, Sparkles, Eye, EyeOff, StickyNote as StickyNoteIcon } from 'lucide-react';
import { useNotes } from '../../context/NotesContext';
import { useAuth } from '../../context/AuthContext';
import { DraggableNote } from './DraggableNote';
import { NoteModal } from './NoteModal';

export const StickyNotesContainer = () => {
  const { isAuthenticated } = useAuth();
  const { notes, overlayVisible, setOverlayVisible } = useNotes();
  const [modalOpen, setModalOpen] = useState(false);
  const [modalType, setModalType] = useState('NOTE');

  if (!isAuthenticated) return null;

  const handleOpenModal = (type) => {
    setModalType(type);
    setModalOpen(true);
  };

  return (
    <>
      {/* Draggable Sticky Notes Layer */}
      {overlayVisible && (
        <div className="pointer-events-auto">
          {notes.map((note) => (
            <DraggableNote key={note.id} note={note} />
          ))}
        </div>
      )}

      {/* Floating Control Dock (Bottom Right) */}
      <div className="fixed bottom-6 right-6 z-40 flex items-center space-x-2 p-1.5 rounded-full bg-slate-900/90 border border-slate-700/80 shadow-2xl backdrop-blur-md">
        <button
          onClick={() => setOverlayVisible(!overlayVisible)}
          title={overlayVisible ? 'Hide Sticky Notes Overlay' : 'Show Sticky Notes Overlay'}
          className={`flex items-center space-x-1 px-3 py-1.5 rounded-full text-xs font-semibold transition-all ${
            overlayVisible
              ? 'bg-slate-800 text-slate-200 hover:bg-slate-700'
              : 'bg-indigo-600/20 text-indigo-300 border border-indigo-500/30'
          }`}
        >
          {overlayVisible ? (
            <>
              <EyeOff className="w-3.5 h-3.5" />
              <span>Hide Notes ({notes.length})</span>
            </>
          ) : (
            <>
              <Eye className="w-3.5 h-3.5" />
              <span>Show Notes ({notes.length})</span>
            </>
          )}
        </button>

        <button
          onClick={() => handleOpenModal('NOTE')}
          title="Add Sticky Note"
          className="flex items-center space-x-1 px-3 py-1.5 rounded-full text-xs font-semibold bg-amber-500/15 text-amber-300 border border-amber-500/30 hover:bg-amber-500/25 transition-all"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>Note</span>
        </button>

        <button
          onClick={() => handleOpenModal('MNEMONIC')}
          title="Add Mnemonic Rule"
          className="flex items-center space-x-1 px-3 py-1.5 rounded-full text-xs font-semibold bg-purple-600 text-white shadow-lg shadow-purple-600/30 hover:bg-purple-500 transition-all"
        >
          <Sparkles className="w-3.5 h-3.5" />
          <span>Mnemonic</span>
        </button>
      </div>

      <NoteModal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        initialType={modalType}
      />
    </>
  );
};
