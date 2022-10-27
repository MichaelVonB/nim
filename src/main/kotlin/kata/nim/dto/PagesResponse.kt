package kata.nim.dto

data class PagesResponse<T>(val totalPages: Int, val totalEntries: Long, val entries: List<T>)