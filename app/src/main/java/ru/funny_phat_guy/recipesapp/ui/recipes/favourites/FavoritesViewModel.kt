package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesRecipeState = MutableLiveData<FavoritesState>(FavoritesState.Loading)
    val favoritesRecipeState get() = _favoritesRecipeState

    private val repository: RecipesRepository = RecipesRepository(application)

    sealed class FavoritesState {
        object Loading : FavoritesState()
        data class Success(val recipes: List<Recipe>) : FavoritesState()
        data class Error(val message: String) : FavoritesState()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favoritesRecipeState.value = FavoritesState.Loading
            try {
                val favorites = repository.getFavorites()
                _favoritesRecipeState.value = FavoritesState.Success(favorites)
            } catch (e: Exception) {
                _favoritesRecipeState.value = FavoritesState.Error("Ошибка: ${e.message}")
            }
        }
    }
}