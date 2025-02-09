package ru.funny_phat_guy.recipesapp.models

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.IOException

object AssetsImageLoader {
    fun loadImage(imageName: String, context: Context?): Drawable? {
        return try {
            val inputStream = context?.assets?.open(imageName)
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("Assets", "Ошибка загрузки изображения", e)
            null
        }
    }
}