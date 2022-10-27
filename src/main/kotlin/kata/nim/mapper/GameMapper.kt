package kata.nim.mapper

import kata.nim.dto.GameDetailedResponse
import kata.nim.dto.GameResponse
import kata.nim.dto.GameRoundResponse
import kata.nim.entity.Game
import kata.nim.entity.GameRound

fun Game.toDetailedDto() = GameDetailedResponse(
    id = id,
    matches = matches,
    minMatchesPerTurn = minMatchesPerTurn,
    maxMatchesPerTurn = maxMatchesPerTurn,
    round = round,
    winner = winner?.title,
    isHard = isHard,
    rounds = rounds.stream().map { r -> r.toDto() }.toList(),
)

fun GameRound.toDto() = GameRoundResponse(
    id = id,
    player = player.title,
    matchesTaken = matchesTaken,
    round = round,
)

fun Game.toDto() = GameResponse(
    id = id,
    matches = matches,
    open = winner == null,
    isHard = isHard
)