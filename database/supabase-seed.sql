-- ChessMaster Pro demo seed data for Supabase
-- Run this after database/supabase-init.sql
-- Seeded login for demo users:
-- email: tacticaltiger@chessmaster.dev
-- password: password
--
-- Other seeded users use the same password.

begin;

insert into public.users (
    id, username, email, password_hash, rating, avatar_url, country, title,
    wins_count, losses_count, draws_count, joined_at, updated_at
) values
    ('11111111-1111-1111-1111-111111111111', 'TacticalTiger', 'tacticaltiger@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1825, 'https://api.dicebear.com/7.x/identicon/svg?seed=TacticalTiger', 'IN', 'CM', 32, 11, 4, '2025-11-10T09:00:00Z', '2026-03-27T09:30:00Z'),
    ('22222222-2222-2222-2222-222222222222', 'MagnusMock', 'magnusmock@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2410, 'https://api.dicebear.com/7.x/identicon/svg?seed=MagnusMock', 'NO', 'GM', 120, 18, 9, '2025-06-14T10:00:00Z', '2026-03-27T08:45:00Z'),
    ('33333333-3333-3333-3333-333333333333', 'EndgameEva', 'endgameeva@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1988, 'https://api.dicebear.com/7.x/identicon/svg?seed=EndgameEva', 'DE', 'WIM', 58, 19, 12, '2025-08-01T12:00:00Z', '2026-03-26T21:15:00Z'),
    ('44444444-4444-4444-4444-444444444444', 'BlitzByte', 'blitzbyte@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1712, 'https://api.dicebear.com/7.x/identicon/svg?seed=BlitzByte', 'US', null, 27, 22, 3, '2025-09-09T07:30:00Z', '2026-03-27T10:12:00Z'),
    ('55555555-5555-5555-5555-555555555555', 'ForkMaster', 'forkmaster@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1654, 'https://api.dicebear.com/7.x/identicon/svg?seed=ForkMaster', 'GB', null, 21, 20, 6, '2025-10-19T16:20:00Z', '2026-03-27T10:10:00Z'),
    ('66666666-6666-6666-6666-666666666666', 'SicilianStorm', 'sicilianstorm@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1545, 'https://api.dicebear.com/7.x/identicon/svg?seed=SicilianStorm', 'IT', null, 14, 18, 4, '2025-12-03T18:10:00Z', '2026-03-25T11:40:00Z'),
    ('77777777-7777-7777-7777-777777777777', 'PuzzlePriya', 'puzzlepriya@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1498, 'https://api.dicebear.com/7.x/identicon/svg?seed=PuzzlePriya', 'IN', null, 11, 14, 9, '2026-01-11T05:45:00Z', '2026-03-24T15:20:00Z'),
    ('88888888-8888-8888-8888-888888888888', 'KnightCrawler', 'knightcrawler@chessmaster.dev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1432, 'https://api.dicebear.com/7.x/identicon/svg?seed=KnightCrawler', 'CA', null, 9, 17, 5, '2026-02-04T14:25:00Z', '2026-03-23T12:50:00Z')
on conflict (id) do update set
    username = excluded.username,
    email = excluded.email,
    password_hash = excluded.password_hash,
    rating = excluded.rating,
    avatar_url = excluded.avatar_url,
    country = excluded.country,
    title = excluded.title,
    wins_count = excluded.wins_count,
    losses_count = excluded.losses_count,
    draws_count = excluded.draws_count,
    joined_at = excluded.joined_at,
    updated_at = excluded.updated_at;

insert into public.user_settings (
    id, user_id, move_sounds, notification_sounds, game_alerts, chat_messages,
    board_theme, default_time_control, created_at, updated_at
) values
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111', true, true, true, true, 'classic', '10+0', '2025-11-10T09:01:00Z', '2026-03-27T09:30:00Z'),
    ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '22222222-2222-2222-2222-222222222222', false, false, true, false, 'midnight', '3+2', '2025-06-14T10:01:00Z', '2026-03-27T08:45:00Z'),
    ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '33333333-3333-3333-3333-333333333333', true, true, true, true, 'wood', '15+10', '2025-08-01T12:01:00Z', '2026-03-26T21:15:00Z'),
    ('aaaaaaa4-aaaa-aaaa-aaaa-aaaaaaaaaaa4', '44444444-4444-4444-4444-444444444444', true, false, true, true, 'neon', '3+0', '2025-09-09T07:31:00Z', '2026-03-27T10:12:00Z'),
    ('aaaaaaa5-aaaa-aaaa-aaaa-aaaaaaaaaaa5', '55555555-5555-5555-5555-555555555555', true, true, false, true, 'classic', '5+3', '2025-10-19T16:21:00Z', '2026-03-27T10:10:00Z'),
    ('aaaaaaa6-aaaa-aaaa-aaaa-aaaaaaaaaaa6', '66666666-6666-6666-6666-666666666666', false, true, true, true, 'tournament', '10+5', '2025-12-03T18:11:00Z', '2026-03-25T11:40:00Z'),
    ('aaaaaaa7-aaaa-aaaa-aaaa-aaaaaaaaaaa7', '77777777-7777-7777-7777-777777777777', true, true, true, false, 'classic', '10+0', '2026-01-11T05:46:00Z', '2026-03-24T15:20:00Z'),
    ('aaaaaaa8-aaaa-aaaa-aaaa-aaaaaaaaaaa8', '88888888-8888-8888-8888-888888888888', true, true, false, true, 'wood', '1+0', '2026-02-04T14:26:00Z', '2026-03-23T12:50:00Z')
on conflict (user_id) do update set
    move_sounds = excluded.move_sounds,
    notification_sounds = excluded.notification_sounds,
    game_alerts = excluded.game_alerts,
    chat_messages = excluded.chat_messages,
    board_theme = excluded.board_theme,
    default_time_control = excluded.default_time_control,
    updated_at = excluded.updated_at;

insert into public.achievements (id, name, description) values
    ('90000000-0000-0000-0000-000000000001', 'First Win', 'Win your first rated or casual game.'),
    ('90000000-0000-0000-0000-000000000002', 'Speed Demon', 'Win a blitz game with under 10 seconds left.'),
    ('90000000-0000-0000-0000-000000000003', 'Analyst', 'Review three completed games in analysis mode.'),
    ('90000000-0000-0000-0000-000000000004', 'Chatty Player', 'Send 25 game chat messages.'),
    ('90000000-0000-0000-0000-000000000005', 'Iron Defender', 'Hold a drawn endgame after being down material.')
on conflict (id) do update set
    name = excluded.name,
    description = excluded.description;

insert into public.user_achievements (id, user_id, achievement_id, earned, earned_at) values
    ('91000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', '90000000-0000-0000-0000-000000000001', true, '2025-11-12T14:00:00Z'),
    ('91000000-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', '90000000-0000-0000-0000-000000000003', true, '2026-02-11T17:20:00Z'),
    ('91000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', '90000000-0000-0000-0000-000000000004', true, '2026-03-01T18:40:00Z'),
    ('91000000-0000-0000-0000-000000000004', '33333333-3333-3333-3333-333333333333', '90000000-0000-0000-0000-000000000005', true, '2026-01-29T21:00:00Z'),
    ('91000000-0000-0000-0000-000000000005', '44444444-4444-4444-4444-444444444444', '90000000-0000-0000-0000-000000000002', true, '2026-03-09T12:05:00Z')
on conflict (user_id, achievement_id) do update set
    earned = excluded.earned,
    earned_at = excluded.earned_at;

insert into public.games (
    id, white_player_id, black_player_id, status, result, result_reason, fen, pgn, history_json,
    time_control, white_time_remaining, black_time_remaining, turn_color, last_move_from, last_move_to,
    last_move_san, rated, draw_offered_by, is_bot_game, bot_level, created_at, updated_at, ended_at
) values
    (
        'a1000000-0000-0000-0000-000000000001',
        '11111111-1111-1111-1111-111111111111',
        '33333333-3333-3333-3333-333333333333',
        'FINISHED',
        'WHITE_WIN',
        'CHECKMATE',
        'move:7:h5:f7',
        'e2-e4 e7-e5 g1-f3 b8-c6 f1-c4 g8-f6 h5-f7',
        '["e2-e4","e7-e5","g1-f3","b8-c6","f1-c4","g8-f6","h5-f7"]',
        '10+0',
        418,
        372,
        'black',
        'h5',
        'f7',
        'Qxf7#',
        true,
        null,
        false,
        null,
        '2026-03-20T18:00:00Z',
        '2026-03-20T18:13:00Z',
        '2026-03-20T18:13:00Z'
    ),
    (
        'a1000000-0000-0000-0000-000000000002',
        '44444444-4444-4444-4444-444444444444',
        '55555555-5555-5555-5555-555555555555',
        'ACTIVE',
        null,
        null,
        'move:6:f1:c4',
        'e2-e4 c7-c5 g1-f3 d7-d6 f1-c4',
        '["e2-e4","c7-c5","g1-f3","d7-d6","f1-c4"]',
        '3+2',
        96,
        104,
        'black',
        'f1',
        'c4',
        'Bc4',
        true,
        null,
        false,
        null,
        '2026-03-27T10:00:00Z',
        '2026-03-27T10:04:00Z',
        null
    ),
    (
        'a1000000-0000-0000-0000-000000000003',
        '11111111-1111-1111-1111-111111111111',
        null,
        'ACTIVE',
        null,
        null,
        'move:4:f1:b5',
        'e2-e4 e7-e5 g1-f3 b8-c6 f1-b5',
        '["e2-e4","e7-e5","g1-f3","b8-c6","f1-b5"]',
        '5+0',
        255,
        268,
        'black',
        'f1',
        'b5',
        'Bb5',
        false,
        null,
        true,
        3,
        '2026-03-27T09:50:00Z',
        '2026-03-27T09:55:00Z',
        null
    ),
    (
        'a1000000-0000-0000-0000-000000000004',
        '66666666-6666-6666-6666-666666666666',
        '11111111-1111-1111-1111-111111111111',
        'FINISHED',
        'DRAW',
        'DRAW_AGREED',
        'move:8:c2:c4',
        'e2-e4 c7-c5 g1-f3 d7-d6 d2-d4 c5-d4 f3-d4 g8-f6 c2-c4',
        '["e2-e4","c7-c5","g1-f3","d7-d6","d2-d4","c5-d4","f3-d4","g8-f6","c2-c4"]',
        '15+10',
        732,
        745,
        'black',
        'c2',
        'c4',
        'c4',
        false,
        null,
        false,
        null,
        '2026-03-18T17:30:00Z',
        '2026-03-18T17:49:00Z',
        '2026-03-18T17:49:00Z'
    )
on conflict (id) do update set
    white_player_id = excluded.white_player_id,
    black_player_id = excluded.black_player_id,
    status = excluded.status,
    result = excluded.result,
    result_reason = excluded.result_reason,
    fen = excluded.fen,
    pgn = excluded.pgn,
    history_json = excluded.history_json,
    time_control = excluded.time_control,
    white_time_remaining = excluded.white_time_remaining,
    black_time_remaining = excluded.black_time_remaining,
    turn_color = excluded.turn_color,
    last_move_from = excluded.last_move_from,
    last_move_to = excluded.last_move_to,
    last_move_san = excluded.last_move_san,
    rated = excluded.rated,
    draw_offered_by = excluded.draw_offered_by,
    is_bot_game = excluded.is_bot_game,
    bot_level = excluded.bot_level,
    updated_at = excluded.updated_at,
    ended_at = excluded.ended_at;

insert into public.game_moves (
    id, game_id, player_id, move_number, move_color, from_square, to_square, promotion, san, fen_after, created_at
) values
    ('b1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 1, 'white', 'e2', 'e4', null, 'e2-e4', 'move:1:e2:e4', '2026-03-20T18:00:10Z'),
    ('b1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', 2, 'black', 'e7', 'e5', null, 'e7-e5', 'move:2:e7:e5', '2026-03-20T18:01:00Z'),
    ('b1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 3, 'white', 'g1', 'f3', null, 'g1-f3', 'move:3:g1:f3', '2026-03-20T18:02:00Z'),
    ('b1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', 4, 'black', 'b8', 'c6', null, 'b8-c6', 'move:4:b8:c6', '2026-03-20T18:03:20Z'),
    ('b1000000-0000-0000-0000-000000000005', 'a1000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 5, 'white', 'f1', 'c4', null, 'f1-c4', 'move:5:f1:c4', '2026-03-20T18:05:00Z'),
    ('b1000000-0000-0000-0000-000000000006', 'a1000000-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', 6, 'black', 'g8', 'f6', null, 'g8-f6', 'move:6:g8:f6', '2026-03-20T18:07:00Z'),
    ('b1000000-0000-0000-0000-000000000007', 'a1000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 7, 'white', 'h5', 'f7', null, 'Qxf7#', 'move:7:h5:f7', '2026-03-20T18:13:00Z'),
    ('b1000000-0000-0000-0000-000000000008', 'a1000000-0000-0000-0000-000000000002', '44444444-4444-4444-4444-444444444444', 1, 'white', 'e2', 'e4', null, 'e2-e4', 'move:1:e2:e4', '2026-03-27T10:00:12Z'),
    ('b1000000-0000-0000-0000-000000000009', 'a1000000-0000-0000-0000-000000000002', '55555555-5555-5555-5555-555555555555', 2, 'black', 'c7', 'c5', null, 'c7-c5', 'move:2:c7:c5', '2026-03-27T10:00:35Z'),
    ('b1000000-0000-0000-0000-000000000010', 'a1000000-0000-0000-0000-000000000002', '44444444-4444-4444-4444-444444444444', 3, 'white', 'g1', 'f3', null, 'g1-f3', 'move:3:g1:f3', '2026-03-27T10:01:10Z'),
    ('b1000000-0000-0000-0000-000000000011', 'a1000000-0000-0000-0000-000000000002', '55555555-5555-5555-5555-555555555555', 4, 'black', 'd7', 'd6', null, 'd7-d6', 'move:4:d7:d6', '2026-03-27T10:02:30Z'),
    ('b1000000-0000-0000-0000-000000000012', 'a1000000-0000-0000-0000-000000000002', '44444444-4444-4444-4444-444444444444', 5, 'white', 'f1', 'c4', null, 'Bc4', 'move:5:f1:c4', '2026-03-27T10:04:00Z'),
    ('b1000000-0000-0000-0000-000000000013', 'a1000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 1, 'white', 'e2', 'e4', null, 'e2-e4', 'move:1:e2:e4', '2026-03-27T09:50:10Z'),
    ('b1000000-0000-0000-0000-000000000014', 'a1000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 2, 'black', 'e7', 'e5', null, 'e7-e5', 'move:2:e7:e5', '2026-03-27T09:51:00Z'),
    ('b1000000-0000-0000-0000-000000000015', 'a1000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 3, 'white', 'g1', 'f3', null, 'g1-f3', 'move:3:g1:f3', '2026-03-27T09:52:00Z'),
    ('b1000000-0000-0000-0000-000000000016', 'a1000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 4, 'black', 'b8', 'c6', null, 'b8-c6', 'move:4:b8:c6', '2026-03-27T09:53:10Z'),
    ('b1000000-0000-0000-0000-000000000017', 'a1000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 5, 'white', 'f1', 'b5', null, 'Bb5', 'move:5:f1:b5', '2026-03-27T09:55:00Z')
on conflict (id) do update set
    game_id = excluded.game_id,
    player_id = excluded.player_id,
    move_number = excluded.move_number,
    move_color = excluded.move_color,
    from_square = excluded.from_square,
    to_square = excluded.to_square,
    promotion = excluded.promotion,
    san = excluded.san,
    fen_after = excluded.fen_after,
    created_at = excluded.created_at;

insert into public.game_chat_messages (id, game_id, sender_id, message, created_at) values
    ('c1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000002', '44444444-4444-4444-4444-444444444444', 'GLHF!', '2026-03-27T10:00:05Z'),
    ('c1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000002', '55555555-5555-5555-5555-555555555555', 'Have fun.', '2026-03-27T10:00:15Z'),
    ('c1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000002', '44444444-4444-4444-4444-444444444444', 'Najdorf incoming?', '2026-03-27T10:01:22Z'),
    ('c1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', '55555555-5555-5555-5555-555555555555', 'Only if you let me.', '2026-03-27T10:01:40Z')
on conflict (id) do update set
    game_id = excluded.game_id,
    sender_id = excluded.sender_id,
    message = excluded.message,
    created_at = excluded.created_at;

commit;
