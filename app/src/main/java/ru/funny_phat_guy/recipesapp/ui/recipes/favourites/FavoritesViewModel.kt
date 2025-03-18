package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.data.STUB
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesRecipeState = MutableLiveData(FavoritesState())
    val favoritesRecipeState get() = _favoritesRecipeState

    private val context get() = getApplication<Application>().applicationContext

    data class FavoritesState(
        val recipe: List<Recipe>? = null
    )

    fun getFavoritesFragment(): Set<Int> {
        val sharedPreferences by lazy {
            context.getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
        }
        val favoriteSet = sharedPreferences.getStringSet(FAVORITES, emptySet()).orEmpty()

        val favoriteSetInt = favoriteSet.mapNotNull { it.toIntOrNull() }.toSet()
        return favoriteSetInt
    }

    fun getFavoriteRecipes(idSet: Set<Int>) {
        val currentState = _favoritesRecipeState.value
        val recipe = STUB.getRecipesByIds(idSet)
        _favoritesRecipeState.value = currentState?.copy(recipe = recipe)
    }
}