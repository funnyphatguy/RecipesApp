package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.data.STUB
import ru.funny_phat_guy.recipesapp.model.Recipe


class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val _allRecipesState = MutableLiveData(ListOfRecipeState())

    val allRecipeState get() = _allRecipesState

    private val context: Context
        get() = getApplication<Application>().applicationContext

    data class ListOfRecipeState(
        val categoryDescription: String? = null,
        val categoryImage: Drawable? = null,
        val recipes: List<Recipe>? = null,
    )

    fun loadRecipesData(categoryId: Int?, categoryImage: String?, categoryDescription: String?) {
        //  TODO: Load from network
        val currentState = _allRecipesState.value
        val recipes = STUB.getRecipesByCategoryId(categoryId)
        val drawable = categoryImage?.let { AssetsImageLoader.loadImage(it, context) }
        _allRecipesState.value = currentState?.copy(
            categoryDescription = categoryDescription,
            recipes = recipes,
            categoryImage = drawable
        )
    }

    fun takeRecipeId(recipeId:Int): Recipe? {
      return STUB.getRecipeById(recipeId)
    }

}

