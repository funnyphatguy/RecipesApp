package ru.funny_phat_guy.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.funny_phat_guy.recipesapp.data.repo.RecipeApiService
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.room.CategoryDatabase
import ru.funny_phat_guy.recipesapp.data.room.RecipeDatabase
import ru.funny_phat_guy.recipesapp.ui.Constants.BASE_URL

class AppContainer(context: Context) {

    val categoryDatabase =
        Room.databaseBuilder(context, CategoryDatabase::class.java, "database-category")
            .allowMainThreadQueries().build()

    val recipeDatabase =
        Room.databaseBuilder(context, RecipeDatabase::class.java, "database-recipe")
            .allowMainThreadQueries().build()


    val categoriesDao = categoryDatabase.categoriesDao()
    val recipeDao = recipeDatabase.recipeDao()

    val contentType = "application/json".toMediaType()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val ioDispatcher = Dispatchers.IO

    private var service: RecipeApiService =
        retrofit.create<RecipeApiService?>(RecipeApiService::class.java)

    val repository = RecipesRepository(
        categoriesDao = categoriesDao,
        recipesDao = recipeDao,
        ioDispatcher = ioDispatcher,
        service = service
    )

    val categoriesViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipesViewModelFactory = RecipesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}