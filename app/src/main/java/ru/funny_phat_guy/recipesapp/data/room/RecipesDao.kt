package ru.funny_phat_guy.recipesapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.funny_phat_guy.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id =:recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE isFavorite=1")
    suspend fun getFavorites(): List<Recipe>

    @Query("UPDATE recipes SET isFavorite =:newValue WHERE id =:recipeId")
    suspend fun setFavorite(recipeId: Int, newValue: Boolean)
}
