package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.RepositoryResult
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesRecipeState = MutableLiveData<FavoritesState>(FavoritesState.Loading)
    val favoritesRecipeState get() = _favoritesRecipeState

    private val repository: RecipesRepository = RecipesRepository()

    private val context get() = getApplication<Application>().applicationContext

    sealed class FavoritesState {
        object Loading : FavoritesState()
        data class Success(val recipes: List<Recipe>) : FavoritesState()
        data class Error(val message: String) : FavoritesState()
    }

    fun getFavoritesFragment(): Set<Int> {
        val sharedPreferences by lazy {
            context.getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
        }
        val favoriteSet = sharedPreferences.getStringSet(FAVORITES, emptySet()).orEmpty()

        val favoriteSetInt = favoriteSet.mapNotNull { it.toIntOrNull() }.toSet()
        return favoriteSetInt
    }

    fun getFavoriteRecipes(idSet: Set<Int>) {
        viewModelScope.launch {
            _favoritesRecipeState.value = FavoritesState.Loading
            when (val result = repository.getRecipesByIds(idSet)) {
                is RepositoryResult.Success -> {
                    _favoritesRecipeState.value = FavoritesState.Success(result.data)
                }

                is RepositoryResult.Error -> {
                    val errorMessage = when (result.exception) {
                        is IOException -> getApplication<Application>().getString(R.string.network_error)
                        else -> getApplication<Application>().getString(R.string.data_error)
                    }
                    _favoritesRecipeState.value = FavoritesState.Error(errorMessage)
                }
            }
        }
    }
}