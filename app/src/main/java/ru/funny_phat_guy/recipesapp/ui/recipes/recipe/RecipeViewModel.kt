package ru.funny_phat_guy.recipesapp.ui.recipes.recipe


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.repo.RepositoryResult
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES
import ru.funny_phat_guy.recipesapp.ui.recipes.recipe.RecipeViewModel.RecipeState.*

class RecipeViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repository: RecipesRepository = RecipesRepository(application)
    private val _recipeState = MutableLiveData<RecipeState>(RecipeState.Loading)

    val recipeState: LiveData<RecipeState> get() = _recipeState

    private val context: Context
        get() = getApplication<Application>().applicationContext

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
    }

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

    private fun getFavorites(): HashSet<String> {
        val favoriteSet = sharedPref.getStringSet(FAVORITES, emptySet()).orEmpty()
        return HashSet(favoriteSet)
    }

    private fun saveFavorites(ides: Set<String>) {
        sharedPref.edit { putStringSet(FAVORITES, ides) }
    }

    fun onFavoritesClicked() {
        val currentState = _recipeState.value ?: return
        if (currentState !is RecipeState.Content) return

        val recipeId = currentState.recipe?.id?.toString() ?: return

        val newFavorites = getFavorites().toMutableSet().apply {
            if (currentState.isFavourites) remove(recipeId) else add(recipeId)
        }
        saveFavorites(newFavorites)
        _recipeState.value = currentState.copy(isFavourites = !currentState.isFavourites)
    }

    fun updatePortionCounter(portionQuantity: Int) {
        val currentState = _recipeState.value ?: return
        if (currentState !is RecipeState.Content) return
        _recipeState.value = currentState.copy(portionsCount = portionQuantity)
    }

    fun loadRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val currentState = _recipeState.value
            val isFavorite = recipe.id.toString() in getFavorites()
            _recipeState.value = Content(
                recipe = recipe,
                isFavourites = isFavorite,
                portionsCount = 1,
                recipeDrawable = recipe.imageUrl
            )

//            //  TODO: Load from network
//            val currentState = _allRecipesState.value
//            val recipes = STUB.getRecipesByCategoryId(categoryId)
//            val drawable = categoryImage?.let { AssetsImageLoader.loadImage(it, context) }
//            _allRecipesState.value = currentState?.copy(
//                categoryDescription = categoryDescription,
//                recipes = recipes,
//                categoryImage = drawable
//            )
        }
    }
}
