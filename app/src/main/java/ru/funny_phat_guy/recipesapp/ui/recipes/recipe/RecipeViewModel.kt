package ru.funny_phat_guy.recipesapp.ui.recipes.recipe


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.STUB
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
    }

    data class recipeState(
        val recipe: Recipe? = null,
        val isFavourites: Boolean = false,
        val portionsCount: Int = 1,
    )

    private val _recipeLiveData = MutableLiveData(recipeState())

    val recipeLiveData: LiveData<recipeState> get() = _recipeLiveData

     private fun getFavorites(): HashSet<String> {
        val favoriteSet = sharedPref.getStringSet(FAVORITES, emptySet()).orEmpty()
        return HashSet(favoriteSet)
    }

    private fun saveFavorites(ides: Set<String>) {
        sharedPref.edit().putStringSet(FAVORITES, ides).apply()
    }

    fun onFavoritesClicked(){
        val currentState = _recipeLiveData.value ?: return
        val recipeId = currentState.recipe?.id?.toString() ?: return

        val newFavorites = getFavorites().toMutableSet().apply {
            if (currentState.isFavourites) remove(recipeId) else add(recipeId)
        }

        saveFavorites(newFavorites)
        _recipeLiveData.value = currentState.copy(isFavourites = !currentState.isFavourites)
        }

    fun loadRecipe(recipeId: Int) {
        //  TODO: Load from network
        val currentState = _recipeLiveData.value
        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = recipe?.id.toString() in getFavorites()

        _recipeLiveData.value = currentState?.copy(
            recipe = recipe,
            isFavourites = isFavorite,
            portionsCount = currentState.portionsCount
        )
    }
}