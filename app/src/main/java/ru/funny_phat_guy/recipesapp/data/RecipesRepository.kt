package ru.funny_phat_guy.recipesapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.BASE_URL
import java.io.IOException
import java.util.concurrent.Executors

class RecipesRepository {

    val contentType = "application/json".toMediaType()

    val threadPool = Executors.newFixedThreadPool(10)

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    var service: RecipeApiService = retrofit.create<RecipeApiService?>(RecipeApiService::class.java)

    fun getRecipesByIds(set: Set<Int>): List<Recipe>? {
        try {
            val stringSet = set.joinToString(",")
            val recipesCall = service.getRecipesByIds(stringSet)
            val recipesResponse = recipesCall.execute()
            val recipes = recipesResponse.body()
            return recipes
        } catch (e: IOException) {
            Log.e("RecipeApiService", "Network error: ${e.message}")
            return null
        } catch (e: RuntimeException) {
            Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
            return null
        }
    }

    fun getCategories(): List<Category>? {
        try {
            val categoriesCall = service.getCategories()
            val categoriesResponse: Response<List<Category>?> = categoriesCall.execute()
            val categories = categoriesResponse.body()
            return categories
        } catch (e: IOException) {
            Log.e("RecipeApiService", "Network error: ${e.message}")
            return null
        } catch (e: RuntimeException) {
            Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
            return null
        }
    }

    fun getRecipeById(burgerRecipeId: Int): Recipe? {
        try {
            val recipeCall = service.getRecipe(burgerRecipeId)
            val recipeResponse = recipeCall.execute()
            val recipe = recipeResponse.body()
            return recipe
        } catch (e: IOException) {
            Log.e("RecipeApiService", "Network error: ${e.message}")
            return null
        } catch (e: RuntimeException) {
            Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
            return null
        }
    }

    fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
        try {
            val recipesByCategory = service.getRecipesById(categoryId)
            val recipesByCategoryResponse = recipesByCategory.execute()
            val recipes = recipesByCategoryResponse.body()
            return recipes
        } catch (e: IOException) {
            Log.e("RecipeApiService", "Network error: ${e.message}")
            return null
        } catch (e: RuntimeException) {
            Log.e("RecipeApiService", "RuntimeException error: ${e.message}")
            return null
        }
    }
}