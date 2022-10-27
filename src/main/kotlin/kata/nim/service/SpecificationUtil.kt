package kata.nim.service

import org.springframework.data.jpa.domain.Specification

fun <T> addAndSpecificationIfNotNull(
    specification: Specification<T>?,
    newSpecification: Specification<T>
): Specification<T> {
    return specification?.and(newSpecification) ?: newSpecification
}