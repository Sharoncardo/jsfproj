import React, { createContext, useContext, useState, useEffect, useCallback, useRef } from 'react';
import { notesApi } from '../api/notesApi';
import { useAuth } from './AuthContext';

const NotesContext = createContext(null);

export const NotesProvider = ({ children }) => {
  const { isAuthenticated } = useAuth();
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [overlayVisible, setOverlayVisible] = useState(true);
  const [activeProblemId, setActiveProblemId] = useState(null);

  // Debounce timers for drag/resize position patches
  const debounceTimers = useRef({});

  const fetchNotes = useCallback(async () => {
    if (!isAuthenticated) {
      setNotes([]);
      return;
    }
    setLoading(true);
    try {
      const res = await notesApi.getNotes();
      if (res.success && res.data) {
        setNotes(res.data);
      }
    } catch (err) {
      console.error('Failed to load sticky notes:', err);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]);

  useEffect(() => {
    fetchNotes();
  }, [fetchNotes]);

  const addNote = async (noteData) => {
    try {
      const res = await notesApi.createNote(noteData);
      if (res.success && res.data) {
        setNotes((prev) => [res.data, ...prev]);
        return res.data;
      }
    } catch (err) {
      console.error('Failed to create note:', err);
      throw err;
    }
  };

  const updateNote = async (id, noteData) => {
    // Optimistic update
    setNotes((prev) =>
      prev.map((n) => (n.id === id ? { ...n, ...noteData, updatedAt: new Date().toISOString() } : n))
    );

    try {
      const res = await notesApi.updateNote(id, noteData);
      if (res.success && res.data) {
        setNotes((prev) => prev.map((n) => (n.id === id ? res.data : n)));
        return res.data;
      }
    } catch (err) {
      console.error('Failed to update note:', err);
      fetchNotes(); // Rollback
      throw err;
    }
  };

  const updateNotePosition = useCallback((id, positionData) => {
    // 1. Optimistic UI update for immediate fluidity
    setNotes((prev) =>
      prev.map((n) =>
        n.id === id
          ? {
              ...n,
              positionX: positionData.positionX ?? n.positionX,
              positionY: positionData.positionY ?? n.positionY,
              width: positionData.width ?? n.width,
              height: positionData.height ?? n.height,
            }
          : n
      )
    );

    // 2. Debounced API call (350ms)
    if (debounceTimers.current[id]) {
      clearTimeout(debounceTimers.current[id]);
    }

    debounceTimers.current[id] = setTimeout(async () => {
      try {
        await notesApi.updatePosition(id, positionData);
      } catch (err) {
        console.error('Failed to persist note position:', err);
      } finally {
        delete debounceTimers.current[id];
      }
    }, 350);
  }, []);

  const deleteNote = async (id) => {
    // Optimistic removal
    setNotes((prev) => prev.filter((n) => n.id !== id));

    try {
      await notesApi.deleteNote(id);
    } catch (err) {
      console.error('Failed to delete note:', err);
      fetchNotes(); // Rollback
      throw err;
    }
  };

  const togglePin = async (id) => {
    const target = notes.find((n) => n.id === id);
    if (!target) return;
    await updateNote(id, { pinned: !target.pinned });
  };

  return (
    <NotesContext.Provider
      value={{
        notes,
        loading,
        overlayVisible,
        setOverlayVisible,
        activeProblemId,
        setActiveProblemId,
        addNote,
        updateNote,
        updateNotePosition,
        deleteNote,
        togglePin,
        refreshNotes: fetchNotes,
      }}
    >
      {children}
    </NotesContext.Provider>
  );
};

export const useNotes = () => {
  const context = useContext(NotesContext);
  if (!context) {
    throw new Error('useNotes must be used within a NotesProvider');
  }
  return context;
};
