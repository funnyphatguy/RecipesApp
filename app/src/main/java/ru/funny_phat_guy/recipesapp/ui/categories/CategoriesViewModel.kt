package ru.funny_phat_guy.recipesapp.ui.categories

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Category

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    val repository: RecipesRepository = RecipesRepository()

    init {
        getCategories()
    }

    private val _allCategoryState = MutableLiveData(CategoriesState())
    val allCategoryState = _allCategoryState

   private val context: Context
       get() = getApplication<Application>().applicationContext

    data class CategoriesState(
        val categories: List<Category>? = null
    )

    fun getCategories() {
        viewModelScope.launch {
            val categories = repository.getCategories() ?: run {
                Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show()
                return@launch
            }
            _allCategoryState.postValue(_allCategoryState.value?.copy(categories = categories))
        }
    }
}