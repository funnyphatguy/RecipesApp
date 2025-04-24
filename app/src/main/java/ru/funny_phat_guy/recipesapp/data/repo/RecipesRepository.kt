package ru.funny_phat_guy.recipesapp.data.repo

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.funny_phat_guy.recipesapp.data.room.CategoriesDao
import ru.funny_phat_guy.recipesapp.data.room.RecipesDao
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
import java.io.IOException

class RecipesRepository(
    private val categoriesDao: CategoriesDao,
    private val recipesDao: RecipesDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val service: RecipeApiService
) {

    suspend fun updateRecipe(recipe: Recipe) {
        try {
            recipesDao.updateRecipe(recipe)
        } catch (e: Exception) {
            Log.e("!!!", "Error in updateRecipe", e)
            emptyList<Recipe>()
        }

    }

    suspend fun getCategories(): RepositoryResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val dataFromCache = categoriesDao.getAll()
                if (dataFromCache.isNotEmpty()) {
                    return@withContext RepositoryResult.Success(dataFromCache)
                }
                val dataFromNet = service.getCategories()
                categoriesDao.insertAll(dataFromNet)
                RepositoryResult.Success(dataFromNet)
            } catch (e: Exception) {
                val fallBackData = categoriesDao.getAll()
                if (fallBackData.isNotEmpty()) {
                    RepositoryResult.Success(fallBackData)
                } else {
                    RepositoryResult.Error(e)
                }
            }
        }
    }

    suspend fun getFavorites(): List<Recipe> {
        return withContext(ioDispatcher) {
            try {
                recipesDao.getFavorites()
            } catch (e: Exception) {
                Log.e("!!!", "Error in getFavorites", e)
                emptyList<Recipe>()
            }
        }
    }

    suspend fun getRecipesFromCache(): List<Recipe> {
        return withContext(ioDispatcher) {
            try {
                recipesDao.getAll()
            } catch (e: Exception) {
                Log.e("!!!", "Error in getRecipesFromCache", e)
                emptyList<Recipe>()
            }
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        withContext(ioDispatcher) {
            val existingRecipes = recipesDao.getAll()
                .associateBy { it.id }
            val recipesToSave = recipes.map { newRecipe ->
                newRecipe.copy(
                    isFavorite = existingRecipes[newRecipe.id]?.isFavorite == true
                )
            }
            recipesDao.insertAll(recipesToSave)
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int?): RepositoryResult<List<Recipe>> {
        return withContext(ioDispatcher) {
            try {
                val recipes = service.getRecipesById(categoryId)
                RepositoryResult.Success(recipes)
            } catch (e: IOException) {
                RepositoryResult.Error(e)
            }
        }
    }
}