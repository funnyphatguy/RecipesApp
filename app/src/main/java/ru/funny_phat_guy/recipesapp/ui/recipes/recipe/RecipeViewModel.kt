package ru.funny_phat_guy.recipesapp.ui.recipes.recipe


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
import ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes.RecipesViewModel.ListOfRecipeState
import ru.funny_phat_guy.recipesapp.ui.recipes.recipe.RecipeViewModel.RecipeState.*

class RecipeViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _recipeState = MutableLiveData<RecipeState>(RecipeState.Loading)

    val recipeState: LiveData<RecipeState> get() = _recipeState

    private val context: Context
        get() = getApplication<Application>().applicationContext

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
    }

    private val repository: RecipesRepository = RecipesRepository()

//    data class RecipeState(
//        val recipe: Recipe? = null,
//        val isFavourites: Boolean = false,
//        val portionsCount: Int = 1,
//        val recipeDrawable: String? = null,
//    )

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

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {

            _recipeState.value = RecipeState.Loading
            when (val recipe = repository.getRecipeById(recipeId)) {
                is RepositoryResult.Success -> {
                    val isFavorite = recipe.data.id.toString() in getFavorites()
                    _recipeState.value = Content(
                        recipe = recipe.data,
                        isFavourites = isFavorite,
                        portionsCount = (_recipeState.value as? RecipeState.Content)?.portionsCount
                            ?: 1,
                        recipeDrawable = recipe.data.imageUrl
                    )
                }

                is RepositoryResult.Error -> {
                    Log.e("Categories", "Loading failed", recipe.exception)
                    val errormessage = when (recipe.exception) {
                        is IOException -> getApplication<Application>().getString(R.string.network_error)
                        else -> getApplication<Application>().getString(R.string.data_error)
                    }
                    _recipeState.value = RecipeState.Error(errormessage)
                }

                null -> Log.i("ERROR", "ERROR")
            }
        }
    }
}
