package ru.funny_phat_guy.recipesapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.funny_phat_guy.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class CategoryDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}