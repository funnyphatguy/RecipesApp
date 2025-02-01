package ru.funny_phat_guy.recipesapp.models

import java.net.URL

data class Recipe(
    val id:Int,
    val title:String,
    val ingredients:List<Ingredient>,
    val method:List<String>,
    val imageURL: String,
)
