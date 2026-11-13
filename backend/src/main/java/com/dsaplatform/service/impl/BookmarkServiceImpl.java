package com.dsaplatform.service.impl;

import com.dsaplatform.dto.bookmark.BookmarkDto;
import com.dsaplatform.dto.bookmark.CreateBookmarkRequest;
import com.dsaplatform.dto.bookmark.UpdateBookmarkRequest;
import com.dsaplatform.dto.common.PageResponse;
import com.dsaplatform.entity.Bookmark;
import com.dsaplatform.entity.User;
import com.dsaplatform.exception.ForbiddenException;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.BookmarkRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookmarkDto createBookmark(UUID userId, CreateBookmarkRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String problemId = request.getProblemId().trim();
        Optional<Bookmark> existing = bookmarkRepository.findByUserIdAndProblemId(userId, problemId);

        Bookmark bookmark;
        if (existing.isPresent()) {
            bookmark = existing.get();
            if (request.getInsight() != null) {
                bookmark.setInsight(request.getInsight());
            }
            if (request.getTags() != null) {
                bookmark.setTags(request.getTags());
            }
        } else {
            bookmark = Bookmark.builder()
                    .user(user)
                    .problemId(problemId)
                    .insight(request.getInsight())
                    .tags(request.getTags())
                    .build();
        }

        Bookmark saved = bookmarkRepository.save(bookmark);
        return BookmarkDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookmarkDto> getBookmarks(UUID userId, Pageable pageable) {
        Page<Bookmark> page = bookmarkRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return PageResponse.of(page.map(BookmarkDto::fromEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public BookmarkDto getBookmarkById(UUID userId, UUID bookmarkId) {
        Bookmark bookmark = findBookmarkAndVerifyOwnership(userId, bookmarkId);
        return BookmarkDto.fromEntity(bookmark);
    }

    @Override
    @Transactional
    public BookmarkDto updateBookmark(UUID userId, UUID bookmarkId, UpdateBookmarkRequest request) {
        Bookmark bookmark = findBookmarkAndVerifyOwnership(userId, bookmarkId);

        if (request.getInsight() != null) {
            bookmark.setInsight(request.getInsight());
        }
        if (request.getTags() != null) {
            bookmark.setTags(request.getTags());
        }

        Bookmark updated = bookmarkRepository.save(bookmark);
        return BookmarkDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteBookmark(UUID userId, UUID bookmarkId) {
        Bookmark bookmark = findBookmarkAndVerifyOwnership(userId, bookmarkId);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(UUID userId, String problemId) {
        return bookmarkRepository.existsByUserIdAndProblemId(userId, problemId);
    }

    private Bookmark findBookmarkAndVerifyOwnership(UUID userId, UUID bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark", "id", bookmarkId));

        if (!bookmark.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to access or modify this bookmark");
        }

        return bookmark;
    }
}
