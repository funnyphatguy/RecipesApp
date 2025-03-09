package ru.funny_phat_guy.recipesapp.ui.recipes.recipe


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.funny_phat_guy.recipesapp.data.STUB
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants

class RecipeViewModel : ViewModel() {

    data class recipeState(
        val recipe: Recipe? = null,
        val isFavourites: Boolean = false,
        val portionsCount: Int = 1,
    )

    private val _recipeLiveData = MutableLiveData(recipeState())

    val recipeLiveData: LiveData<recipeState> get() = _recipeLiveData

    init {
        Log.i(Constants.LOG_INFO_TAG, "Info from VM")

        _recipeLiveData.value = recipeState(isFavourites = true)
    }

    fun loadRecipe(recipeId: Int){
      //  TODO: Load from network
       val recipe =  STUB.getRecipeById(recipeId)
        _recipeLiveData.value = recipeState(recipe=recipe)
    }
}