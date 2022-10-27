package kata.nim.dto

import java.util.*

data class GameResponse(val id: UUID, val matches: Int, val open: Boolean)