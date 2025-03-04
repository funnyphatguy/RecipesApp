package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.funny_phat_guy.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val favourites: Boolean = false,
        val portionsCount: Int? = 1,
    ) {
    }
}