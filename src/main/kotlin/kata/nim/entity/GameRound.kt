package kata.nim.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "game_round")
data class GameRound(

    @Enumerated(EnumType.STRING)
    val player: GamePlayer,
    val matchesTaken: Int,
    val round: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null

    @ManyToOne()
    @JoinColumn(name = "fk_game")
    var game: Game? = null
}
