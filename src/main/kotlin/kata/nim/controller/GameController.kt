package kata.nim.controller

import kata.nim.dto.CreateGameRequest
import kata.nim.dto.GameDetailedResponse
import kata.nim.dto.GameResponse
import kata.nim.errorhandling.BadRequestException
import kata.nim.mapper.toDetailedDto
import kata.nim.mapper.toDto
import kata.nim.service.GameService
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/game")
class GameController(private val gameService: GameService) {


    @GetMapping
    fun getGames(): MutableList<GameResponse>? {
        return gameService.getGames().stream()
            .map { g -> g.toDto() }.toList()
    }

    @GetMapping("{id}")
    fun getGame(@PathVariable @NotNull id: UUID): GameDetailedResponse? {
        val game = gameService.getGame(id)
        return game?.toDetailedDto()
    }

    @PostMapping
    fun createGame(@RequestBody createGameRequest: CreateGameRequest?): GameDetailedResponse {
        return gameService.addGame(
            createGameRequest?.initialMatches,
            createGameRequest?.minMatchesPerTurn,
            createGameRequest?.maxMatchesPerTurn,
            createGameRequest?.isHard
        ).toDetailedDto()
    }

    @PutMapping("{id}")
    fun takeTurn(@PathVariable id: UUID, @RequestParam matchesTaken: Int): GameDetailedResponse {
        val game = gameService.getGame(id) ?: throw BadRequestException("Game with id: $id could not be found")
        return gameService.takeTurn(game, matchesTaken).toDetailedDto()
    }
}