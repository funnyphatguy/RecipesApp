package ru.funny_phat_guy.recipesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.funny_phat_guy.recipesapp.di.RecipeModule

@HiltAndroidApp
class RecipesApplication() : Application()