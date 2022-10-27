package kata.nim.controller

import kata.nim.dto.CreateGameRequest
import kata.nim.dto.GameDetailedResponse
import kata.nim.dto.GameResponse
import kata.nim.mapper.GameMapper
import kata.nim.service.GameService
import org.jetbrains.annotations.NotNull
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/game")
class GameController(private val gameService: GameService) {

    val mapper: GameMapper = Mappers.getMapper(GameMapper::class.java)

    @GetMapping
    fun getGames(): MutableList<GameResponse>? {
        return gameService.getGames().stream()
            .map { g -> GameResponse(g.id, g.matches, g.winner == null) }.toList()
    }

    @GetMapping("{id}")
    fun getGame(@PathVariable @NotNull id: UUID): GameDetailedResponse? {
        val game = gameService.getGame(id)
        return if (game != null) mapper.toDto(game) else null
    }

    @PostMapping
    fun createGame(@RequestBody() createGameRequest: CreateGameRequest?): GameDetailedResponse {
        return mapper.toDto(
            gameService.addGame(
                createGameRequest?.initialMatches,
                createGameRequest?.minMatchesPerTurn,
                createGameRequest?.maxMatchesPerTurn,
                createGameRequest?.isHard
            )
        )
    }

    @PutMapping("{id}")
    fun takeTurn(@PathVariable id: UUID, @RequestParam matchesTaken: Int): GameDetailedResponse {
        return mapper.toDto(gameService.takeTurn(id, matchesTaken))
    }
}