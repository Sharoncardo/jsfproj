package com.dsaplatform.controller;

import com.dsaplatform.dto.bookmark.BookmarkDto;
import com.dsaplatform.dto.bookmark.CreateBookmarkRequest;
import com.dsaplatform.dto.bookmark.UpdateBookmarkRequest;
import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.common.PageResponse;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmarks", description = "Endpoints for saving problem insights, tags and quick references")
@SecurityRequirement(name = "bearerAuth")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    @Operation(summary = "Add or upsert a bookmark with personal insights and tags")
    public ResponseEntity<ApiResponse<BookmarkDto>> createBookmark(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateBookmarkRequest request) {
        BookmarkDto dto = bookmarkService.createBookmark(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Bookmark saved successfully", dto));
    }

    @GetMapping
    @Operation(summary = "Get paginated bookmarks for the current user")
    public ResponseEntity<ApiResponse<PageResponse<BookmarkDto>>> getBookmarks(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<BookmarkDto> page = bookmarkService.getBookmarks(principal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single bookmark by ID")
    public ResponseEntity<ApiResponse<BookmarkDto>> getBookmarkById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {
        BookmarkDto dto = bookmarkService.getBookmarkById(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update bookmark insight text and tags")
    public ResponseEntity<ApiResponse<BookmarkDto>> updateBookmark(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookmarkRequest request) {
        BookmarkDto dto = bookmarkService.updateBookmark(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Bookmark updated successfully", dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bookmark")
    public ResponseEntity<ApiResponse<Void>> deleteBookmark(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {
        bookmarkService.deleteBookmark(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.ok("Bookmark deleted successfully", null));
    }

    @GetMapping("/check")
    @Operation(summary = "Check if a problem is bookmarked by current user")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String problemId) {
        boolean bookmarked = bookmarkService.isBookmarked(principal.getId(), problemId);
        return ResponseEntity.ok(ApiResponse.ok(bookmarked));
    }
}
