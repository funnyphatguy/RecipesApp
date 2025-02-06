package ru.funny_phat_guy.recipesapp.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

object AssetsImageLoader {
    fun loadImage(context: Context, imageName: String): Bitmap? {
        return try {
            val inputStream = context.assets.open(imageName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            null
        }
    }
}