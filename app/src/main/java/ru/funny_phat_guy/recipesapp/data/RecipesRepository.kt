package ru.funny_phat_guy.recipesapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.BASE_URL
import java.io.IOException


class RecipesRepository {

    val contentType = "application/json".toMediaType()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    var service: RecipeApiService = retrofit.create<RecipeApiService?>(RecipeApiService::class.java)

    suspend fun getRecipesByIds(set: Set<Int>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val stringSet = set.joinToString(",")
                val recipesCall = service.getRecipesByIds(stringSet)
                val recipesResponse = recipesCall.execute()
                recipesResponse.body()
            } catch (e: IOException) {
                Log.e("RecipeApiService", "Network error: ${e.message}")
                null
            } catch (e: RuntimeException) {
                Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
                null
            }
        }

    }

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val categoriesCall = service.getCategories()
                val categoriesResponse: Response<List<Category>?> = categoriesCall.execute()
                categoriesResponse.body()
            } catch (e: IOException) {
                Log.e("RecipeApiService", "Network error: ${e.message}")
                null
            } catch (e: RuntimeException) {
                Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
                null
            }
        }
    }

    suspend fun getRecipeById(burgerRecipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val recipeCall = service.getRecipe(burgerRecipeId)
                val recipeResponse = recipeCall.execute()
                recipeResponse.body()
            } catch (e: IOException) {
                Log.e("RecipeApiService", "Network error: ${e.message}")
                null
            } catch (e: RuntimeException) {
                Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
                null
            }
        }

    }

    suspend fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val recipesByCategory = service.getRecipesById(categoryId)
                val recipesByCategoryResponse = recipesByCategory.execute()
                recipesByCategoryResponse.body()
            } catch (e: IOException) {
                Log.e("RecipeApiService", "Network error: ${e.message}")
                null
            } catch (e: RuntimeException) {
                Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
                null
            }
        }

    }
}