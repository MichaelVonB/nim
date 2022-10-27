package kata.nim.errorhandling

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@ResponseBody()
class BadRequestException(message: String?) : RuntimeException(message) {
}