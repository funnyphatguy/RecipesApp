package ru.funny_phat_guy.recipesapp.ui.categories

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.RepositoryResult
import ru.funny_phat_guy.recipesapp.model.Category

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    val repository: RecipesRepository = RecipesRepository()

    private val _allCategoryState = MutableLiveData<CategoriesState>(CategoriesState.Loading)
    val allCategoryState = _allCategoryState

    init {
        getCategories()
    }


    sealed class CategoriesState {
        object Loading : CategoriesState()
        data class Success(val categories: List<Category>) : CategoriesState()
        data class Error(val message: String) : CategoriesState()
    }


    fun getCategories() {
        viewModelScope.launch {
            _allCategoryState.value = CategoriesState.Loading
            when (val result = repository.getCategories()) {
                is RepositoryResult.Success -> {
                    _allCategoryState.value = CategoriesState.Success(result.data)
                }

                is RepositoryResult.Error -> {
                    val errorMessage = when (result.exception) {
                        is IOException -> getApplication<Application>().getString(R.string.network_error)
                        else -> getApplication<Application>().getString(R.string.data_error)
                    }
                    _allCategoryState.value = CategoriesState.Error(errorMessage)
                }
            }
        }
    }
}
