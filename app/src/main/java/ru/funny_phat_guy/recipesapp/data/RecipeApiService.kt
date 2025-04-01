package ru.funny_phat_guy.recipesapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe


interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipe(@Path("id") id: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") ids: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategory(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesById(@Path("id") id: Int?): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}