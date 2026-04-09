-- ChessMaster Pro Supabase bootstrap
-- Run this in the Supabase SQL editor for project tckcfpscxwzmetudpikc.

create extension if not exists pgcrypto;

create table if not exists public.users (
    id uuid primary key default gen_random_uuid(),
    username varchar(50) not null unique,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    rating integer not null default 1500,
    avatar_url varchar(255),
    country varchar(4),
    title varchar(20),
    wins_count integer not null default 0,
    losses_count integer not null default 0,
    draws_count integer not null default 0,
    joined_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists public.user_settings (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null unique references public.users(id) on delete cascade,
    move_sounds boolean not null default true,
    notification_sounds boolean not null default true,
    game_alerts boolean not null default true,
    chat_messages boolean not null default true,
    board_theme varchar(30) not null default 'classic',
    default_time_control varchar(15) not null default '10+0',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists public.games (
    id uuid primary key default gen_random_uuid(),
    white_player_id uuid references public.users(id) on delete set null,
    black_player_id uuid references public.users(id) on delete set null,
    status varchar(20) not null,
    result varchar(20),
    result_reason varchar(40),
    fen varchar(255) not null,
    pgn text not null,
    history_json text not null,
    time_control varchar(15) not null,
    white_time_remaining integer not null,
    black_time_remaining integer not null,
    turn_color varchar(5) not null,
    last_move_from varchar(5),
    last_move_to varchar(5),
    last_move_san varchar(30),
    rated boolean not null default false,
    draw_offered_by uuid,
    is_bot_game boolean not null default false,
    bot_level integer,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    ended_at timestamptz
);

create table if not exists public.game_moves (
    id uuid primary key default gen_random_uuid(),
    game_id uuid not null references public.games(id) on delete cascade,
    player_id uuid not null references public.users(id) on delete restrict,
    move_number integer not null,
    move_color varchar(5) not null,
    from_square varchar(5) not null,
    to_square varchar(5) not null,
    promotion varchar(5),
    san varchar(30) not null,
    fen_after varchar(255) not null,
    created_at timestamptz not null default now()
);

create table if not exists public.game_chat_messages (
    id uuid primary key default gen_random_uuid(),
    game_id uuid not null references public.games(id) on delete cascade,
    sender_id uuid not null references public.users(id) on delete restrict,
    message varchar(500) not null,
    created_at timestamptz not null default now()
);

create table if not exists public.achievements (
    id uuid primary key default gen_random_uuid(),
    name varchar(80) not null unique,
    description varchar(255) not null
);

create table if not exists public.user_achievements (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.users(id) on delete cascade,
    achievement_id uuid not null references public.achievements(id) on delete cascade,
    earned boolean not null default true,
    earned_at timestamptz not null default now(),
    constraint user_achievements_user_achievement_unique unique (user_id, achievement_id)
);

create index if not exists idx_users_rating on public.users (rating desc);
create index if not exists idx_games_white_player on public.games (white_player_id);
create index if not exists idx_games_black_player on public.games (black_player_id);
create index if not exists idx_games_status on public.games (status);
create index if not exists idx_games_updated_at on public.games (updated_at desc);
create index if not exists idx_game_moves_game_move_number on public.game_moves (game_id, move_number);
create index if not exists idx_game_chat_messages_game_created_at on public.game_chat_messages (game_id, created_at);
create index if not exists idx_user_achievements_user on public.user_achievements (user_id);

comment on table public.users is 'Application users for ChessMaster Pro.';
comment on table public.user_settings is 'Per-user frontend settings persisted by the backend.';
comment on table public.games is 'Authoritative game state for multiplayer and bot games.';
comment on table public.game_moves is 'Sequential move history for each game.';
comment on table public.game_chat_messages is 'Persisted in-game chat messages.';
comment on table public.achievements is 'Achievement catalog.';
comment on table public.user_achievements is 'Achievement unlocks per user.';
