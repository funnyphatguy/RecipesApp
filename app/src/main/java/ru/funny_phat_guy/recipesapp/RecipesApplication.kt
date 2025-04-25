package ru.funny_phat_guy.recipesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.funny_phat_guy.recipesapp.di.RecipeModule

@HiltAndroidApp
class RecipesApplication() : Application() {

    lateinit var appContainer: RecipeModule

    override fun onCreate() {
        super.onCreate()
        appContainer = RecipeModule(this)
    }
}