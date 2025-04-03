package ru.funny_phat_guy.recipesapp.ui.recipes.recipe


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES
import ru.funny_phat_guy.recipesapp.ui.Constants.LOAD_IMAGE_ERROR_LOG
import androidx.core.content.edit
import kotlinx.serialization.descriptors.PrimitiveKind
import ru.funny_phat_guy.recipesapp.data.RecipesRepository

class RecipeViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())

    val recipeState: LiveData<RecipeState> get() = _recipeState

    private val context: Context
        get() = getApplication<Application>().applicationContext

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
    }

    private val repository: RecipesRepository = RecipesRepository()

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavourites: Boolean = false,
        val portionsCount: Int = 1,
        val recipeDrawable: Drawable? = null,
    )

    private fun getFavorites(): HashSet<String> {
        val favoriteSet = sharedPref.getStringSet(FAVORITES, emptySet()).orEmpty()
        return HashSet(favoriteSet)
    }

    private fun saveFavorites(ides: Set<String>) {
        sharedPref.edit() { putStringSet(FAVORITES, ides) }
    }

    fun onFavoritesClicked() {
        val currentState = _recipeState.value ?: return
        val recipeId = currentState.recipe?.id?.toString() ?: return

        val newFavorites = getFavorites().toMutableSet().apply {
            if (currentState.isFavourites) remove(recipeId) else add(recipeId)
        }
        saveFavorites(newFavorites)
        _recipeState.value = currentState.copy(isFavourites = !currentState.isFavourites)
    }

    fun updatePortionCounter(portionQuantity: Int) {
        _recipeState.value = _recipeState.value?.copy(portionsCount = portionQuantity)
    }

    fun loadRecipe(recipeId: Int) {
        repository.threadPool.submit {
            val currentState = _recipeState.value
            val recipe = repository.getRecipeById(recipeId)
            val isFavorite = recipe?.id.toString() in getFavorites()
            val recipeImage = recipe?.imageUrl?.let {
                try {
                    AssetsImageLoader.loadImage(it, context)
                } catch (e: Exception) {
                    Log.e(LOAD_IMAGE_ERROR_LOG, "Error in load image ${e.message}", e)
                    null
                }
            }
            if (recipeImage == null) {
                Log.e(LOAD_IMAGE_ERROR_LOG, "LoadImage is null for ${recipe?.id}")
            }
            _recipeState.postValue(
                currentState?.copy(
                    recipe = recipe,
                    isFavourites = isFavorite,
                    portionsCount = currentState.portionsCount,
                    recipeDrawable = recipeImage,
                )
            )
        } ?: Toast.makeText(context, "Рецепт не найден", Toast.LENGTH_SHORT).show()

    }
}