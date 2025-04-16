package ru.funny_phat_guy.recipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val ingredients: List<Ingredient>,
    @ColumnInfo val method: List<String>,
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val isFavorite: Boolean = false
) : Parcelable
