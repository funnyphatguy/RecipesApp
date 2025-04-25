package ru.funny_phat_guy.recipesapp.di

interface Factory<T> {
    fun create(): T
}