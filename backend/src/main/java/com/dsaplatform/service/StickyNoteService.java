package com.dsaplatform.service;

import com.dsaplatform.dto.note.CreateNoteRequest;
import com.dsaplatform.dto.note.StickyNoteDto;
import com.dsaplatform.dto.note.UpdateNoteRequest;
import com.dsaplatform.dto.note.UpdatePositionRequest;

import java.util.List;
import java.util.UUID;

public interface StickyNoteService {
    List<StickyNoteDto> getNotes(UUID userId, String problemId);
    StickyNoteDto getNoteById(UUID userId, UUID noteId);
    StickyNoteDto createNote(UUID userId, CreateNoteRequest request);
    StickyNoteDto updateNote(UUID userId, UUID noteId, UpdateNoteRequest request);
    StickyNoteDto updatePosition(UUID userId, UUID noteId, UpdatePositionRequest request);
    void deleteNote(UUID userId, UUID noteId);
}
