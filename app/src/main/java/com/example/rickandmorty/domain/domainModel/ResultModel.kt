package com.example.rickandmorty.domain.domainModel

sealed class ResultModel<out T> {
    data class Success<out T>(val data: T) : ResultModel<T>()
    data class Error(val exception: Exception) : ResultModel<Nothing>()

    val isSuccess: Boolean get() = this is Success<T>
    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }
}