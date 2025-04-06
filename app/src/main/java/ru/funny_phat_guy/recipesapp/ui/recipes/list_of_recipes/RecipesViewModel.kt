package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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
        val categoryPictureUrl: String? = null,
        val recipes: List<Recipe>? = null,
    )

    fun loadRecipesData(categoryId: Int?, categoryImage: String?, categoryDescription: String?) {
        viewModelScope.launch {
            val currentState = _allRecipesState.value
            val recipes = repository.getRecipesByCategoryId(categoryId) ?: run {
                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
                return@launch
            }

            _allRecipesState.postValue(
                currentState?.copy(
                    categoryDescription = categoryDescription,
                    recipes = recipes,
                    categoryPictureUrl = categoryImage
                )
            )
        }
    }
}

