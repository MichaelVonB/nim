package kata.nim.entity

import kata.nim.errorhandling.BadRequestException
import java.util.*
import javax.persistence.*

@Entity
class Game(matches: Int?, minMatchesPerTurn: Int?, maxMatchesPerTurn: Int?, hard: Boolean?) {

    @Id
    var id: UUID = UUID.randomUUID()

    var matches = matches ?: 13
    var minMatchesPerTurn = minMatchesPerTurn ?: 1
    var maxMatchesPerTurn = maxMatchesPerTurn ?: 3

    init {
        if (this.minMatchesPerTurn >= this.maxMatchesPerTurn || this.minMatchesPerTurn < 0) {
            throw BadRequestException("Invalid Combination of min- and max Matcher per Turn")
        }
        if (this.matches < this.minMatchesPerTurn) {
            throw BadRequestException("Game should allow at least one turn")
        }
    }

    var round = 0

    @Enumerated(EnumType.STRING)
    var winner: GamePlayer? = null
    var isHard = hard ?: false // if game is set to hard, the computer will try to win the game

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    var rounds = listOf<GameRound>()
}