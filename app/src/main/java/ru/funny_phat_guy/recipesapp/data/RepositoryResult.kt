package ru.funny_phat_guy.recipesapp.data

// создаем обертку для результатов запросов
sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val exception: Throwable) : RepositoryResult<Nothing>()
}