CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rating INTEGER NOT NULL DEFAULT 1500,
    avatar_url VARCHAR(255),
    country VARCHAR(4),
    title VARCHAR(20),
    wins_count INTEGER NOT NULL DEFAULT 0,
    losses_count INTEGER NOT NULL DEFAULT 0,
    draws_count INTEGER NOT NULL DEFAULT 0,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_settings (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    move_sounds BOOLEAN NOT NULL DEFAULT TRUE,
    notification_sounds BOOLEAN NOT NULL DEFAULT TRUE,
    game_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    chat_messages BOOLEAN NOT NULL DEFAULT TRUE,
    board_theme VARCHAR(30) NOT NULL DEFAULT 'classic',
    default_time_control VARCHAR(15) NOT NULL DEFAULT '10+0',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS games (
    id UUID PRIMARY KEY,
    white_player_id UUID REFERENCES users(id),
    black_player_id UUID REFERENCES users(id),
    status VARCHAR(20) NOT NULL,
    result VARCHAR(20),
    result_reason VARCHAR(40),
    fen VARCHAR(255) NOT NULL,
    pgn TEXT NOT NULL,
    history_json TEXT NOT NULL,
    time_control VARCHAR(15) NOT NULL,
    white_time_remaining INTEGER NOT NULL,
    black_time_remaining INTEGER NOT NULL,
    turn_color VARCHAR(5) NOT NULL,
    last_move_from VARCHAR(5),
    last_move_to VARCHAR(5),
    last_move_san VARCHAR(30),
    rated BOOLEAN NOT NULL DEFAULT FALSE,
    draw_offered_by UUID,
    is_bot_game BOOLEAN NOT NULL DEFAULT FALSE,
    bot_level INTEGER,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ended_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS game_moves (
    id UUID PRIMARY KEY,
    game_id UUID NOT NULL REFERENCES games(id),
    player_id UUID NOT NULL REFERENCES users(id),
    move_number INTEGER NOT NULL,
    move_color VARCHAR(5) NOT NULL,
    from_square VARCHAR(5) NOT NULL,
    to_square VARCHAR(5) NOT NULL,
    promotion VARCHAR(5),
    san VARCHAR(30) NOT NULL,
    fen_after VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS game_chat_messages (
    id UUID PRIMARY KEY,
    game_id UUID NOT NULL REFERENCES games(id),
    sender_id UUID NOT NULL REFERENCES users(id),
    message VARCHAR(500) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS achievements (
    id UUID PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_achievements (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    achievement_id UUID NOT NULL REFERENCES achievements(id),
    earned BOOLEAN NOT NULL DEFAULT TRUE,
    earned_at TIMESTAMP WITH TIME ZONE NOT NULL
);
