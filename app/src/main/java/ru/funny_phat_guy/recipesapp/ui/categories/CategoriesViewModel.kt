package ru.funny_phat_guy.recipesapp.ui.categories

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.model.Category

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val _allCategoryState = MutableLiveData(CategoriesState())
    val allCategoryState = _allCategoryState

    private val context get() = getApplication<Application>().applicationContext

    val repository: RecipesRepository = RecipesRepository()

    data class CategoriesState(
        val categories: List<Category>? = null
    )

    fun getCategories() {
        repository.threadPool.submit {
            val currentState = _allCategoryState.value
            val categories = repository.getCategories()
            Log.i("HERE", "$categories")
            _allCategoryState.postValue(currentState?.copy(categories = categories))
        } ?: Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
    }

    fun getCategoryById(categoryId: Int): Category? {
        val future = repository.threadPool.submit<Category?> {
            repository.getCategories()?.find { it.id == categoryId }
        }
        if (future == null) {
            Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
        }
        return future.get()

    }
}