package ru.funny_phat_guy.recipesapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe


interface RecipeApiService {
    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") id: Int): Recipe

    @GET("recipes")
    suspend fun getRecipesByIds(@Query("ids") ids: String): List<Recipe>

    @GET("category/{id}")
    suspend fun getCategory(@Path("id") id: Int): List<Category>

    @GET("category/{id}/recipes")
    suspend fun getRecipesById(@Path("id") id: Int?): List<Recipe>

    @GET("category")
    suspend fun getCategories(): List<Category>

}