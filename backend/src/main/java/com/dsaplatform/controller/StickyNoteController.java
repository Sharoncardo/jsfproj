package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.note.CreateNoteRequest;
import com.dsaplatform.dto.note.StickyNoteDto;
import com.dsaplatform.dto.note.UpdateNoteRequest;
import com.dsaplatform.dto.note.UpdatePositionRequest;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.StickyNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "Sticky Notes & Mnemonics", description = "Endpoints for draggable floating sticky notes and mnemonics")
@SecurityRequirement(name = "bearerAuth")
public class StickyNoteController {

    private final StickyNoteService stickyNoteService;

    @GetMapping
    @Operation(summary = "Get all sticky notes for the current user, optionally filtered by problemId")
    public ResponseEntity<ApiResponse<List<StickyNoteDto>>> getNotes(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String problemId) {
        List<StickyNoteDto> notes = stickyNoteService.getNotes(principal.getId(), problemId);
        return ResponseEntity.ok(ApiResponse.ok(notes));
    }

    @PostMapping
    @Operation(summary = "Create a new sticky note or mnemonic")
    public ResponseEntity<ApiResponse<StickyNoteDto>> createNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateNoteRequest request) {
        StickyNoteDto note = stickyNoteService.createNote(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Sticky note created successfully", note));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single sticky note by id")
    public ResponseEntity<ApiResponse<StickyNoteDto>> getNoteById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {
        StickyNoteDto note = stickyNoteService.getNoteById(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.ok(note));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sticky note content, title, color or pin status")
    public ResponseEntity<ApiResponse<StickyNoteDto>> updateNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNoteRequest request) {
        StickyNoteDto note = stickyNoteService.updateNote(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Sticky note updated successfully", note));
    }

    @PatchMapping("/{id}/position")
    @Operation(summary = "Update sticky note position and dimensions during drag/resize")
    public ResponseEntity<ApiResponse<StickyNoteDto>> updatePosition(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePositionRequest request) {
        StickyNoteDto note = stickyNoteService.updatePosition(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Note position updated successfully", note));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sticky note")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {
        stickyNoteService.deleteNote(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.ok("Sticky note deleted successfully", null));
    }
}
