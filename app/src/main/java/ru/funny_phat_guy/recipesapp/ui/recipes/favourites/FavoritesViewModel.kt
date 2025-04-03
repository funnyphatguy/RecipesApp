package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesRecipeState = MutableLiveData(FavoritesState())
    val favoritesRecipeState get() = _favoritesRecipeState

    private val repository: RecipesRepository = RecipesRepository()

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
        repository.threadPool.submit {
            val currentState = _favoritesRecipeState.value
            val recipe = repository.getRecipesByIds(idSet)
            _favoritesRecipeState.postValue(currentState?.copy(recipe = recipe))
        } ?: Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
    }
}