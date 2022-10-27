package kata.nim.dto

import java.util.*

data class GameRoundResponse(
    val id: UUID,
    val player: String,
    val matchesTaken: Int,
    val round: Int
)