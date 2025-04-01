package ru.funny_phat_guy.recipesapp.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.funny_phat_guy.recipesapp.data.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.STUB

import ru.funny_phat_guy.recipesapp.model.Category

class CategoriesViewModel() : ViewModel() {

    private val _allCategoryState = MutableLiveData(CategoriesState())
    val allCategoryState = _allCategoryState

    data class CategoriesState(
        val categories: List<Category>? = STUB.getCategories()
    )

    fun getCategoryById(categoryId: Int): Category? {
        return STUB.getCategories().find { it.id == categoryId }
    }



}