package com.dsaplatform.service.impl;

import com.dsaplatform.dto.note.CreateNoteRequest;
import com.dsaplatform.dto.note.StickyNoteDto;
import com.dsaplatform.dto.note.UpdateNoteRequest;
import com.dsaplatform.dto.note.UpdatePositionRequest;
import com.dsaplatform.entity.StickyNote;
import com.dsaplatform.entity.User;
import com.dsaplatform.exception.ForbiddenException;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.StickyNoteRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.StickyNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StickyNoteServiceImpl implements StickyNoteService {

    private final StickyNoteRepository stickyNoteRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<StickyNoteDto> getNotes(UUID userId, String problemId) {
        List<StickyNote> notes;
        if (problemId != null && !problemId.isBlank()) {
            notes = stickyNoteRepository.findAllByUserIdAndProblemId(userId, problemId.trim());
        } else {
            notes = stickyNoteRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        }

        return notes.stream().map(StickyNoteDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StickyNoteDto getNoteById(UUID userId, UUID noteId) {
        StickyNote note = findNoteAndVerifyOwnership(userId, noteId);
        return StickyNoteDto.fromEntity(note);
    }

    @Override
    @Transactional
    public StickyNoteDto createNote(UUID userId, CreateNoteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        StickyNote note = StickyNote.builder()
                .user(user)
                .problemId(request.getProblemId())
                .title(request.getTitle().trim())
                .content(request.getContent())
                .type(request.getType())
                .color(request.getColor() != null ? request.getColor() : "#FEF08A")
                .positionX(request.getPositionX() != null ? request.getPositionX() : 100.0)
                .positionY(request.getPositionY() != null ? request.getPositionY() : 100.0)
                .width(request.getWidth() != null ? request.getWidth() : 280.0)
                .height(request.getHeight() != null ? request.getHeight() : 240.0)
                .pinned(Boolean.TRUE.equals(request.getPinned()))
                .build();

        StickyNote saved = stickyNoteRepository.save(note);
        return StickyNoteDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public StickyNoteDto updateNote(UUID userId, UUID noteId, UpdateNoteRequest request) {
        StickyNote note = findNoteAndVerifyOwnership(userId, noteId);

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            note.setTitle(request.getTitle().trim());
        }
        if (request.getContent() != null) {
            note.setContent(request.getContent());
        }
        if (request.getType() != null) {
            note.setType(request.getType());
        }
        if (request.getColor() != null) {
            note.setColor(request.getColor());
        }
        if (request.getPinned() != null) {
            note.setPinned(request.getPinned());
        }

        StickyNote updated = stickyNoteRepository.save(note);
        return StickyNoteDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public StickyNoteDto updatePosition(UUID userId, UUID noteId, UpdatePositionRequest request) {
        StickyNote note = findNoteAndVerifyOwnership(userId, noteId);

        note.setPositionX(request.getPositionX());
        note.setPositionY(request.getPositionY());

        if (request.getWidth() != null && request.getWidth() > 0) {
            note.setWidth(request.getWidth());
        }
        if (request.getHeight() != null && request.getHeight() > 0) {
            note.setHeight(request.getHeight());
        }

        StickyNote updated = stickyNoteRepository.save(note);
        return StickyNoteDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteNote(UUID userId, UUID noteId) {
        StickyNote note = findNoteAndVerifyOwnership(userId, noteId);
        stickyNoteRepository.delete(note);
    }

    private StickyNote findNoteAndVerifyOwnership(UUID userId, UUID noteId) {
        StickyNote note = stickyNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("StickyNote", "id", noteId));

        if (!note.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to access or modify this sticky note");
        }

        return note;
    }
}
