package kata.nim.service

import kata.nim.entity.Game
import kata.nim.entity.GameRound
import kata.nim.errorhandling.BadRequestException
import kata.nim.repository.GameRepository
import kata.nim.repository.GameRoundRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.max

@Service
class GameService(private val gameRepository: GameRepository, private val gameRoundRepository: GameRoundRepository) {


    fun addGame(initialMatches: Int?, minMatchesPerTurn: Int?, maxMatchesPerTurn: Int?, hard: Boolean?): Game {
        val game = Game(initialMatches, minMatchesPerTurn, maxMatchesPerTurn, hard)
        gameRepository.save(game)
        return game
    }

    fun getGame(id: UUID): Game? {
        return gameRepository.findByIdOrNull(id)
    }

    fun getGames(): MutableList<Game> {
        return gameRepository.findAll()
    }

    fun takeTurn(gameId: UUID, matchesTaken: Int): Game {
        val game = gameRepository.findByIdOrNull(gameId)
            ?: throw BadRequestException("Game with id: $gameId could not be found")
        if (game.winner != null) {
            throw BadRequestException("Game is finished, player ${game.winner} won!")
        }
        if (matchesTaken > game.maxMatchesPerTurn || matchesTaken < game.minMatchesPerTurn) {
            throw BadRequestException("Need to take between ${game.minMatchesPerTurn} and ${game.maxMatchesPerTurn} Matches")
        }
        addTurn(game, "Player", matchesTaken)
        if (game.matches <= 0) {
            game.winner = "Computer"
            gameRepository.save(game)
            return game
        }
        //Computer Turn;
        val computerMatches =
            if (game.isHard) getOptimalTurn(game.matches, game.maxMatchesPerTurn, game.minMatchesPerTurn) else IntRange(
                game.minMatchesPerTurn,
                game.maxMatchesPerTurn
            ).random()
        addTurn(game, "Computer", computerMatches)
        if (game.matches <= 0) {
            game.winner = "Player"
        }
        gameRepository.save(game)
        return game
    }

    private fun addTurn(game: Game, player: String, matchesTaken: Int) {
        val playerRound = GameRound(player, matchesTaken, +game.round)
        playerRound.game = game
        game.matches -= matchesTaken
        game.rounds += playerRound
        game.round++
        gameRoundRepository.save(playerRound)
    }


    private fun getOptimalTurn(matches: Int, maxMatches: Int, minMatches: Int): Int {
        return max((matches - minMatches) % (maxMatches + 1), minMatches)
    }
}