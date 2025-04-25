package ru.funny_phat_guy.recipesapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.repo.RepositoryResult
import ru.funny_phat_guy.recipesapp.model.Category

class CategoriesViewModel(
    private val repository: RecipesRepository
) : ViewModel() {


    private val _allCategoryState = MutableLiveData<CategoriesState>(CategoriesState.Loading)
    val allCategoryState: LiveData<CategoriesState> = _allCategoryState

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
            when (val result = repository.getCategories()) {
                is RepositoryResult.Success -> {
                    _allCategoryState.value = CategoriesState.Success(result.data)
                }

                is RepositoryResult.Error -> {
                    Log.e("Categories", "Loading failed", result.exception)
                }
            }
        }
    }
}
