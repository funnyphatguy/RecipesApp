package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Recipe
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipesRepository
) :
    ViewModel() {

    private val _favoritesRecipeState = MutableLiveData<FavoritesState>(FavoritesState.Loading)
    val favoritesRecipeState get() = _favoritesRecipeState

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