package ru.funny_phat_guy.recipesapp.ui.recipes.recipe


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.recipes.recipe.RecipeViewModel.RecipeState.Content

class RecipeViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository: RecipesRepository = RecipesRepository(application)
    private val _recipeState = MutableLiveData<RecipeState>(RecipeState.Loading)

    val recipeState: LiveData<RecipeState> get() = _recipeState

    sealed class RecipeState {
        object Loading : RecipeState()
        data class Content(
            val recipe: Recipe? = null,
            val isFavourites: Boolean = false,
            val portionsCount: Int = 1,
            val recipeDrawable: String? = null,
        ) : RecipeState()

        data class Error(val message: String) : RecipeState()
    }

    fun onFavoritesClicked() {
        viewModelScope.launch {
            val currentState = _recipeState.value
            val currentContent = currentState as? Content ?: return@launch
            val currentRecipe = currentContent.recipe ?: return@launch

            val updatedRecipe = currentRecipe.copy(isFavorite = !currentRecipe.isFavorite)

            repository.updateRecipe(updatedRecipe)

            _recipeState.value = currentContent.copy(
                recipe = updatedRecipe,
                isFavourites = updatedRecipe.isFavorite,
                recipeDrawable = updatedRecipe.imageUrl
            )
        }
    }

    fun updatePortionCounter(portionQuantity: Int) {
        val currentState = _recipeState.value ?: return
        if (currentState !is RecipeState.Content) return
        _recipeState.value = currentState.copy(portionsCount = portionQuantity)
    }

    fun loadRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val recipeFromDB = repository.getRecipeById(recipe.id)
            viewModelScope.launch {
                _recipeState.value = Content(
                    recipe = recipeFromDB,
                    isFavourites = recipeFromDB.isFavorite,
                    portionsCount = 1,
                    recipeDrawable = recipeFromDB.imageUrl
                )
            }
        }

    }
}
