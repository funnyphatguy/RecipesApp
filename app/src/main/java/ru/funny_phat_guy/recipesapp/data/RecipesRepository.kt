package ru.funny_phat_guy.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants.BASE_URL

class RecipesRepository {

    val contentType = "application/json".toMediaType()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    var service: RecipeApiService = retrofit.create<RecipeApiService?>(RecipeApiService::class.java)

    fun getRecipesByIds(set: Set<Int>): List<Recipe>? {
        val stringSet = set.toString()
        val recipesCall = service.getRecipesByIds(stringSet)
        val recipesResponse = recipesCall.execute()
        val recipes = recipesResponse.body()
        return recipes
    }

    fun getCategories(): List<Category>? {
        val categoriesCall = service.getCategories()
        val categoriesResponse: Response<List<Category>?> = categoriesCall.execute()
        val categories = categoriesResponse.body()
        return categories
    }

    fun getRecipeById(burgerRecipeId: Int): Recipe? {
        val recipeCall = service.getRecipe(burgerRecipeId)
        val recipeResponse = recipeCall.execute()
        val recipe = recipeResponse.body()
        return recipe
    }

    fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
      val recipesByCategory = service.getRecipesById(categoryId)
        val recipesByCategoryResponse = recipesByCategory.execute()
        val recipes = recipesByCategoryResponse.body()
        return recipes
    }
}