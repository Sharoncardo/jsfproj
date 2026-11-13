package com.dsaplatform.service;

import com.dsaplatform.dto.bookmark.BookmarkDto;
import com.dsaplatform.dto.bookmark.CreateBookmarkRequest;
import com.dsaplatform.dto.bookmark.UpdateBookmarkRequest;
import com.dsaplatform.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookmarkService {
    BookmarkDto createBookmark(UUID userId, CreateBookmarkRequest request);
    PageResponse<BookmarkDto> getBookmarks(UUID userId, Pageable pageable);
    BookmarkDto getBookmarkById(UUID userId, UUID bookmarkId);
    BookmarkDto updateBookmark(UUID userId, UUID bookmarkId, UpdateBookmarkRequest request);
    void deleteBookmark(UUID userId, UUID bookmarkId);
    boolean isBookmarked(UUID userId, String problemId);
}
