-- Flyway Migration V1: Initialize Database Schema

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    display_name VARCHAR(100),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_login_at TIMESTAMP WITH TIME ZONE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Progress Track table
CREATE TABLE progress_tracks (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    problem_id VARCHAR(100) NOT NULL,
    topic VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    attempts INTEGER NOT NULL DEFAULT 0,
    time_spent_seconds BIGINT NOT NULL DEFAULT 0,
    solved_at TIMESTAMP WITH TIME ZONE,
    last_attempted_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_progress_user_problem UNIQUE (user_id, problem_id)
);

CREATE INDEX idx_progress_user ON progress_tracks(user_id);
CREATE INDEX idx_progress_topic ON progress_tracks(topic);
CREATE INDEX idx_progress_status ON progress_tracks(status);
CREATE INDEX idx_progress_solved_at ON progress_tracks(solved_at);

-- Sticky Notes table
CREATE TABLE sticky_notes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    problem_id VARCHAR(100),
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'NOTE',
    color VARCHAR(20) NOT NULL DEFAULT '#FEF08A',
    position_x DOUBLE PRECISION NOT NULL DEFAULT 100.0,
    position_y DOUBLE PRECISION NOT NULL DEFAULT 100.0,
    width DOUBLE PRECISION NOT NULL DEFAULT 280.0,
    height DOUBLE PRECISION NOT NULL DEFAULT 240.0,
    pinned BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_notes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_notes_user ON sticky_notes(user_id);
CREATE INDEX idx_notes_problem ON sticky_notes(problem_id);

-- Bookmarks table
CREATE TABLE bookmarks (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    problem_id VARCHAR(100) NOT NULL,
    insight TEXT,
    tags VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_bookmarks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_bookmark_user_problem UNIQUE (user_id, problem_id)
);

CREATE INDEX idx_bookmarks_user ON bookmarks(user_id);
CREATE INDEX idx_bookmarks_problem ON bookmarks(problem_id);
