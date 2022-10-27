package kata.nim.entity

import kata.nim.errorhandling.BadRequestException
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

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
    }

    var round = 0
    var winner: String? = null
    var isHard = hard ?: false // if game is set to hard, the computer will try to win the game

    @OneToMany(mappedBy = "game")
    var rounds = listOf<GameRound>()
}