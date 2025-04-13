package ru.funny_phat_guy.recipesapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.funny_phat_guy.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes")
    fun getAll(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<Recipe>)
}
