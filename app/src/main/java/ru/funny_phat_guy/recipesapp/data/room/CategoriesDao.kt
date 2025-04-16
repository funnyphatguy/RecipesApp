package ru.funny_phat_guy.recipesapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.funny_phat_guy.recipesapp.model.Category

    @Dao
    interface CategoriesDao {
        @Query("SELECT * FROM categories")
        suspend fun getAll(): List<Category>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(categories: List<Category>)

    }
