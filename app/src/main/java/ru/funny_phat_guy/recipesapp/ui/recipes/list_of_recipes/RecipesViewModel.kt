package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Recipe


class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val _allRecipesState = MutableLiveData(ListOfRecipeState())
    private val repository: RecipesRepository = RecipesRepository()

    val allRecipeState get() = _allRecipesState

    private val context: Context
        get() = getApplication<Application>().applicationContext

    data class ListOfRecipeState(
        val categoryDescription: String? = null,
        val categoryImage: Drawable? = null,
        val recipes: List<Recipe>? = null,
    )

    fun loadRecipesData(categoryId: Int?, categoryImage: String?, categoryDescription: String?) {
        repository.threadPool.submit {
            val currentState = _allRecipesState.value
            val recipes = repository.getRecipesByCategoryId(categoryId)
            val drawable = categoryImage?.let { AssetsImageLoader.loadImage(it, context) }
            _allRecipesState.postValue(
                currentState?.copy(
                    categoryDescription = categoryDescription,
                    recipes = recipes,
                    categoryImage = drawable
                )
            )
        } ?: Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()

    }

    fun takeRecipeId(recipeId: Int): Recipe? {
        return repository.getRecipeById(recipeId)
    }

}

