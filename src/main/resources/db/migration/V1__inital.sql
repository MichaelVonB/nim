create Table game
(
    id                   UUID DEFAULT random_uuid() PRIMARY KEY,
    matches              TINYINT,
    min_matches_per_turn TINYINT,
    max_matches_per_turn TINYINT,
    round                TINYINT,
    winner               VARCHAR(255),
    is_hard              BOOLEAN
);

create Table game_round
(
    id            UUID DEFAULT random_uuid() PRIMARY KEY,
    fk_game       UUID REFERENCES game (id),
    player        VARCHAR(255),
    matches_taken TINYINT,
    round         TINYINT
)
