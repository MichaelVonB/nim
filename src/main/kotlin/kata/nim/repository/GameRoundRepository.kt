package kata.nim.repository

import kata.nim.entity.GameRound
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameRoundRepository : JpaRepository<GameRound, UUID>
