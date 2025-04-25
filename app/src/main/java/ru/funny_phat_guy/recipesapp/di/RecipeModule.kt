package ru.funny_phat_guy.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.funny_phat_guy.recipesapp.data.repo.RecipeApiService
import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.data.room.CategoriesDao
import ru.funny_phat_guy.recipesapp.data.room.CategoryDatabase
import ru.funny_phat_guy.recipesapp.data.room.RecipeDatabase
import ru.funny_phat_guy.recipesapp.data.room.RecipesDao
import ru.funny_phat_guy.recipesapp.ui.Constants.BASE_URL

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule() {

    @Provides
    fun provideCategoryDataBase(@ApplicationContext context: Context): CategoryDatabase =
        Room.databaseBuilder(context, CategoryDatabase::class.java, "database-category")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun provideRecipeDataBase(@ApplicationContext context: Context): RecipeDatabase =
        Room.databaseBuilder(context, RecipeDatabase::class.java, "database-recipe")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun provideCategoryDao(categoryDatabase: CategoryDatabase): CategoriesDao =
        categoryDatabase.categoriesDao()

    @Provides
    fun provideRecipeDao(recipeDatabase: RecipeDatabase): RecipesDao = recipeDatabase.recipeDao()

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
        return retrofit
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit):RecipeApiService {
        return retrofit.create<RecipeApiService?>(RecipeApiService::class.java)
    }

}