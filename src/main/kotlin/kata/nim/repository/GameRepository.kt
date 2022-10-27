package kata.nim.repository

import kata.nim.entity.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameRepository : JpaRepository<Game, UUID>
