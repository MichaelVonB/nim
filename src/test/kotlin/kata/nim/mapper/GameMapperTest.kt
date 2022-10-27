package kata.nim.mapper

import kata.nim.entity.Game
import kata.nim.entity.GamePlayer
import kata.nim.entity.GameRound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
internal class GameMapperTest {

    @Test
    fun mapGameEntityToDetailed() {
        val minMatches = IntRange(1, 3).random()
        val maxMatches = IntRange(minMatches + 1, minMatches + 4).random()
        val matches = IntRange(maxMatches * 2, maxMatches * 4).random()
        val game = Game(matches, minMatches, maxMatches, false)
        game.id = UUID.randomUUID()
        val gameDetailed = game.toDetailedDto()
        assertEquals(gameDetailed.id, game.id)
        assertEquals(gameDetailed.matches, matches)
        assertEquals(gameDetailed.minMatchesPerTurn, minMatches)
        assertEquals(gameDetailed.maxMatchesPerTurn, maxMatches)
        assertEquals(gameDetailed.round, game.round)
        assertEquals(gameDetailed.winner, game.winner?.title)
        assertEquals(gameDetailed.isHard, game.isHard)
    }

    @ParameterizedTest(name = "map Game Entity")
    @NullSource
    @EnumSource(GamePlayer::class)
    fun mapGameEntity(player: GamePlayer?) {
        val game = Game(null, null, null, null)
        game.matches = 0
        game.winner = player
        game.id = UUID.randomUUID()
        val gameResponse = game.toDto()

        assertEquals(gameResponse.id, game.id)
        assertEquals(gameResponse.matches, 0)
        assertEquals(gameResponse.open, player == null)
    }

    @Test
    fun mapGameRoundEntity() {
        val matchesTaken = 2
        val round = (0..5).random()
        val gameRound = GameRound(GamePlayer.PLAYER, matchesTaken, round)
        val gameRoundResponse = gameRound.toDto()
        assertEquals(gameRoundResponse.id, gameRound.id)
        assertEquals(gameRoundResponse.player, gameRound.player.title)
        assertEquals(gameRoundResponse.matchesTaken, matchesTaken)
        assertEquals(gameRoundResponse.round, round)
    }

    @Test
    fun gameShouldMapGameRound() {
        val game = Game(null, null, null, null)
        val gameRounds = 3
        for (i in (1..gameRounds)) {
            game.rounds += GameRound(GamePlayer.PLAYER, i, i)
        }
        val gameResponse = game.toDetailedDto()
        assertEquals(gameResponse.rounds.size, gameRounds)
    }
}