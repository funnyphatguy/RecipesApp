package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.repo.RepositoryResult
import ru.funny_phat_guy.recipesapp.model.Recipe


class RecipesViewModel(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _allRecipesState = MutableLiveData<ListOfRecipeState>(ListOfRecipeState.Loading)
    val allRecipeState get() = _allRecipesState

    sealed class ListOfRecipeState {
        object Loading : ListOfRecipeState()
        data class Content(
            val categoryDescription: String,
            val categoryPictureUrl: String,
            val recipes: List<Recipe>,
        ) : ListOfRecipeState()

        data class Error(val message: String) : ListOfRecipeState()
    }

    fun loadRecipesData(categoryId: Int?, categoryImage: String, categoryDescription: String) {
        viewModelScope.launch {
            _allRecipesState.value = ListOfRecipeState.Loading
            val dataFromCache = repository.getRecipesFromCache()
            if (dataFromCache.isNotEmpty()) {
                _allRecipesState.value = ListOfRecipeState.Content(
                    categoryDescription = categoryDescription,
                    categoryPictureUrl = categoryImage,
                    recipes = dataFromCache
                )
            }
            when (val result = repository.getRecipesByCategoryId(categoryId)) {
                is RepositoryResult.Success -> {
                    repository.saveRecipesToCache(recipes = result.data)
                    _allRecipesState.value = ListOfRecipeState.Content(
                        categoryDescription = categoryDescription,
                        categoryPictureUrl = categoryImage,
                        recipes = result.data
                    )
                }

                is RepositoryResult.Error -> {
                    Log.e("Categories", "Loading failed", result.exception)
                }
            }
        }
    }
}

