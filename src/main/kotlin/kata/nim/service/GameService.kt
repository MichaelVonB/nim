package kata.nim.service

import kata.nim.entity.Game
import kata.nim.entity.GamePlayer
import kata.nim.entity.GameRound
import kata.nim.errorhandling.BadRequestException
import kata.nim.repository.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.Integer.min
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

    fun getGames(
        open: Boolean? = null,
        isHard: Boolean? = null,
        winner: GamePlayer? = null,
        pageSize: Int = 25,
        pageNumber: Int = 0
    ): Page<Game> {
        var specification: Specification<Game>? = null
        if (open != null) specification = addAndSpecificationIfNotNull(specification, gameIsOpen(open))
        if (isHard != null) specification = addAndSpecificationIfNotNull(specification, gameIsHard(isHard))
        if (winner != null) specification = addAndSpecificationIfNotNull(specification, withWinner(winner))
        return gameRepository.findAll(specification, PageRequest.of(pageNumber, pageSize))

    }

    fun takeTurn(game: Game, matchesTaken: Int): Game {
        if (game.winner != null) {
            throw BadRequestException("Game is finished, player ${game.winner} won!")
        }
        if (matchesTaken > game.maxMatchesPerTurn || matchesTaken < game.minMatchesPerTurn) {
            throw BadRequestException("Need to take between ${game.minMatchesPerTurn} and ${game.maxMatchesPerTurn} Matches")
        }
        addTurn(game, GamePlayer.PLAYER, matchesTaken)
        if (game.matches <= 0) {
            game.winner = GamePlayer.COMPUTER

            gameRepository.save(game)
            return game
        }
        //Computer Turn;
        val computerMatches =
            if (game.isHard) getOptimalTurn(game.matches, game.maxMatchesPerTurn, game.minMatchesPerTurn) else IntRange(
                game.minMatchesPerTurn,
                game.maxMatchesPerTurn
            ).random()
        addTurn(game, GamePlayer.COMPUTER, computerMatches)
        if (game.matches <= 0) {
            game.winner = GamePlayer.PLAYER
        }
        gameRepository.save(game)
        return game
    }

    private fun addTurn(game: Game, player: GamePlayer, matchesTaken: Int) {
        val matchesTaken = min(
            matchesTaken,
            game.matches
        ) //as minMatchesPerTurn can be greater than one, a valid turn could lead to negative matches
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