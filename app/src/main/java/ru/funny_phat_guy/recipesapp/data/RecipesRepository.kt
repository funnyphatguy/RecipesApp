package ru.funny_phat_guy.recipesapp.data

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.BASE_URL
import java.io.IOException

class RecipesRepository(context: Context) {

    private val applicationContext = context.applicationContext

    private val contentType = "application/json".toMediaType()


   private val categoryDatabase: CategoryDatabase by lazy {
       Room.databaseBuilder(
           applicationContext,
           CategoryDatabase::class.java, "database-category"
       ).build()
   }

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val ioDispatcher = Dispatchers.IO

    private var service: RecipeApiService =
        retrofit.create<RecipeApiService?>(RecipeApiService::class.java)

    suspend fun getRecipesByIds(set: Set<Int>): RepositoryResult<List<Recipe>> {
        return withContext(ioDispatcher) {
            try {
                val stringSet = set.joinToString(",")
                val recipes = service.getRecipesByIds(stringSet)
                RepositoryResult.Success(recipes)
            } catch (e: IOException) {
                Log.e("RecipeApiService", "Network error: ${e.message}")
                RepositoryResult.Error(e)
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            categoryDatabase.categoriesDao().getAll()
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>){
        return withContext(ioDispatcher) {
            categoryDatabase.categoriesDao().insertAll(categories)
        }
    }

    suspend fun getCategories(): RepositoryResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val categories = service.getCategories()
                RepositoryResult.Success(categories)
            } catch (e: Exception) {
                RepositoryResult.Error(e)
            }
        }
    }

    suspend fun getRecipeById(burgerRecipeId: Int): RepositoryResult<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val recipe = service.getRecipe(burgerRecipeId)
                RepositoryResult.Success(recipe)
            } catch (e: IOException) {
                RepositoryResult.Error(e)
            }
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