package kata.nim.service

import kata.nim.entity.Game
import kata.nim.entity.GamePlayer
import kata.nim.errorhandling.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
internal class GameServiceTest {

    @Autowired
    lateinit var gameService: GameService

    @Test
    fun createTestWithNoArgsShouldCreateStandardGame() {
        val standardGame = createStandardGame()

        assertNull(standardGame.winner)
        assertNotNull(standardGame.id)
        assertEquals(standardGame.matches, 13)
        assertEquals(standardGame.minMatchesPerTurn, 1)
        assertEquals(standardGame.maxMatchesPerTurn, 3)
    }

    @Test
    fun creatingGameWithParamsShouldCreateCorrectGame() {
        val initialMatches = 20
        val minMatches = 2
        val maxMatches = 4
        val hard = true
        val customGame = gameService.addGame(initialMatches, minMatches, maxMatches, hard)
        assertNull(customGame.winner)
        assertNotNull(customGame.id)
        assertEquals(customGame.matches, initialMatches)
        assertEquals(customGame.minMatchesPerTurn, minMatches)
        assertEquals(customGame.maxMatchesPerTurn, maxMatches)
        assertEquals(customGame.isHard, hard)
    }

    @Test
    fun creatingGameWithMinMatchesGreaterMaxShouldThrow() {
        val block: () -> Unit = { gameService.addGame(14, 2, 1, false) }
        assertThrows(
            BadRequestException::class.java,
            block
        )
    }

    @Test
    fun creatingGameWithNoMatchesShouldThrow() {
        val block: () -> Unit = { gameService.addGame(0, 1, 3, false) }
        assertThrows(
            BadRequestException::class.java,
            block
        )
    }

    @Test
    fun getGameShouldFindCorrectGame() {
        val standardGame = createStandardGame()
        val game = gameService.getGame(standardGame.id)

        assertNotNull(game)
        assertNotNull(game?.id)
        assertEquals(standardGame.id, game?.id)
    }

    @Test
    fun findGameWithRandomIdShouldReturnNull() {
        val game = gameService.getGame(UUID.randomUUID())
        assertNull(game)
    }


    @Test
    fun getGamesShouldReturnAllGames() {
        val numberOfGames = IntRange(1, 10).random()
        for (i in (0..numberOfGames)) createStandardGame()
        val games = gameService.getGames()

        assertTrue(games.size >= numberOfGames)
    }


    @Test
    fun takeAllowedTurnShouldReturnGameWithTwoRounds() {
        val standardGame = createStandardGame()
        val standardMatches = standardGame.matches
        val gameAfterTurn = gameService.takeTurn(standardGame, 1)
        val matchesTaken = gameAfterTurn.rounds.map { r -> r.matchesTaken }.sum()
        assertNotNull(gameAfterTurn)
        assertNotNull(gameAfterTurn.rounds)
        assertTrue(gameAfterTurn.rounds.size == 2)
        assertTrue(gameAfterTurn.matches == standardMatches - matchesTaken)
    }

    @Test
    fun takeForbiddenTurnShouldThrow() {
        val standardGame = createStandardGame()
        val block: () -> Unit = { gameService.takeTurn(standardGame, standardGame.maxMatchesPerTurn + 1) }
        assertThrows(BadRequestException::class.java, block)
    }

    @Test
    fun takeTurnInFinishedGameShouldThrow() {
        val standardGame = createStandardGame()
        standardGame.matches = 0
        standardGame.winner = GamePlayer.COMPUTER
        val block: () -> Unit = { gameService.takeTurn(standardGame, standardGame.maxMatchesPerTurn) }
        assertThrows(BadRequestException::class.java, block)
    }

    @Test
    fun ComputerShouldForcePlayerInLosingPositionInHardGame() {
        val minMatches = 1
        val maxMatches = 3
        val game = gameService.addGame(5, minMatches, maxMatches, true)
        val gameAfterTurn = gameService.takeTurn(game, IntRange(minMatches, maxMatches).random())
        assertNull(gameAfterTurn.winner)
        assertEquals(gameAfterTurn.matches, 1)
    }

    @Test
    fun ComputerShouldAlwaysWinStandardGameOnHard() {
        val minMatches = 1
        val maxMatches = 3
        var game = gameService.addGame(13, minMatches, maxMatches, true)
        while (game.winner == null) {
            game = gameService.takeTurn(game, IntRange(minMatches, maxMatches).random())
        }
        assertEquals(game.winner, GamePlayer.COMPUTER)
        assertTrue(game.matches <= 0)
        assertEquals(game.rounds[game.rounds.size - 1].player, GamePlayer.PLAYER)
    }

    @ParameterizedTest(name = "ComputerShouldAwaysTakeValidTurn")
    @ValueSource(booleans = [false, true])
    fun ComputerShouldAlwaysTakeValidTurn(hard: Boolean) {
        var game = createRandomGame(hard)
        game = gameService.takeTurn(game, (game.minMatchesPerTurn..game.maxMatchesPerTurn).random())
        assertEquals(game.rounds.size, 2)
        assertTrue(game.rounds[1].matchesTaken <= game.maxMatchesPerTurn)
        assertTrue(game.rounds[1].matchesTaken >= game.minMatchesPerTurn)
    }


    private fun createStandardGame(): Game {
        return gameService.addGame(null, null, null, null)
    }

    private fun createRandomGame(hard: Boolean = false): Game {
        val minMatches = IntRange(1, 3).random()
        val maxMatches = IntRange(minMatches + 1, minMatches + 4).random()
        val matches = IntRange(maxMatches * 2, maxMatches * 4).random()
        return gameService.addGame(matches, minMatches, maxMatches, hard)
    }
}