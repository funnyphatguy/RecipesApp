package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.funny_phat_guy.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val favourites: Boolean = false,
        val portionsCount: Int? = 1,
    ) {
    }

    private val _recipeLiveData = MutableLiveData<RecipeState>(RecipeState())

    val recipeLiveData: LiveData<RecipeState> get() = _recipeLiveData


    init {
        Log.i("!!!", "Info from VM")

        _recipeLiveData.value = RecipeState(favourites = true)

    }
}