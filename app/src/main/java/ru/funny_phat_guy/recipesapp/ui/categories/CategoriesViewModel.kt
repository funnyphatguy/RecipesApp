package ru.funny_phat_guy.recipesapp.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Category

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val _allCategoryState = MutableLiveData(CategoriesState())
    val allCategoryState = _allCategoryState

    val repository: RecipesRepository = RecipesRepository()

    data class CategoriesState(
        val categories: List<Category>? = null
    )

    fun getCategories() {
        viewModelScope.launch {
            val categories = repository.getCategories()
            Log.i("HERE", "$categories")
            _allCategoryState.postValue(_allCategoryState.value?.copy(categories = categories))
        }
    }
}