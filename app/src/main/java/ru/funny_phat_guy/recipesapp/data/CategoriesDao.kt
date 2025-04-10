package ru.funny_phat_guy.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.funny_phat_guy.recipesapp.model.Category


    @Dao
    interface CategoriesDao {
        @Query("SELECT * FROM categories")
        suspend fun getAll(): List<Category>

//        @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//        fun loadAllByIds(userIds: IntArray): List<User>
//
//        @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//                "last_name LIKE :last LIMIT 1")
//        fun findByName(first: String, last: String): User

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(categories: List<Category>)

//        @Delete
//        fun delete(user: User)

        // Пока методы закоментил, вдруг понадобятся в будущем
    }
