package ru.funny_phat_guy.recipesapp.data.room

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.funny_phat_guy.recipesapp.model.Ingredient

class Converters {
    @TypeConverter
    fun fromIngredientsList(value: List<Ingredient>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIngredientsList(value: String): List<Ingredient> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromMethodList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMethodList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}