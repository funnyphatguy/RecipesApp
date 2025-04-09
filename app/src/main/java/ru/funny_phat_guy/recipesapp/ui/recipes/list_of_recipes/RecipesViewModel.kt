package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.RepositoryResult
import ru.funny_phat_guy.recipesapp.model.Recipe


class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val _allRecipesState = MutableLiveData<ListOfRecipeState>(ListOfRecipeState.Loading)
    private val repository: RecipesRepository = RecipesRepository()

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
            when (val result = repository.getRecipesByCategoryId(categoryId)) {
                is RepositoryResult.Success -> {
                    _allRecipesState.value = ListOfRecipeState.Content(
                        categoryDescription = categoryDescription,
                        categoryPictureUrl = categoryImage,
                        recipes = result.data
                    )
                }

                is RepositoryResult.Error -> {
                    Log.e("Categories", "Loading failed", result.exception)
                    val errorMessage = when (result.exception) {
                        is IOException -> getApplication<Application>().getString(R.string.network_error)
                        else -> getApplication<Application>().getString(R.string.data_error)
                    }
                    _allRecipesState.value = ListOfRecipeState.Error(errorMessage)
                }
            }
        }
    }
}

