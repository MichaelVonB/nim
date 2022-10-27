package kata.nim.repository

import kata.nim.entity.Game
import kata.nim.entity.GamePlayer
import org.springframework.data.jpa.domain.Specification

fun gameIsOpen(isOpen: Boolean): Specification<Game> {
    return Specification<Game> { root, _, builder ->
        if (isOpen) {
            builder.isNull(root.get<Game>("winner"))

        } else {
            builder.isNotNull(root.get<Game>("winner"))
        }
    }
}

fun gameIsHard(isHard: Boolean): Specification<Game> {
    return Specification<Game> { root, _, builder ->
        builder.equal(root.get<Game>("isHard"), isHard)
    }
}

fun withWinner(winner: GamePlayer): Specification<Game> {
    return Specification<Game> { root, _, builder ->
        builder.equal(root.get<Game>("winner"), winner)
    }
}