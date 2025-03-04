package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.funny_phat_guy.recipesapp.model.Ingredient

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val imageUrl: String? = null,
        val favourites:Boolean = false,
        val title: String? = null,
        val seekbarCount:Int? = null,
        val ingredients: List<Ingredient> = emptyList(),
        val method: List<String> = emptyList(),
    ) {
    }

}