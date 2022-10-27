package kata.nim.dto

class CreateGameRequest(
    val initialMatches: Int?,
    val minMatchesPerTurn: Int?,
    val maxMatchesPerTurn: Int?,
    val isHard: Boolean?
)