package ru.funny_phat_guy.recipesapp

import android.app.Application
import ru.funny_phat_guy.recipesapp.di.AppContainer

class RecipesApplication() : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}