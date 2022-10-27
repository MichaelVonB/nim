package kata.nim.dto

import java.util.*

data class GameDetailedResponse(
    val id: UUID,
    val matches: Int,
    val minMatchesPerTurn: Int,
    val maxMatchesPerTurn: Int,
    val round: Int,
    val winner: String,
    val isHard: Boolean,
    val rounds: List<GameRoundResponse>
)
