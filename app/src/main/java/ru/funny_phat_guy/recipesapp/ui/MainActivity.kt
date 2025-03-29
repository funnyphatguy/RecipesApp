package ru.funny_phat_guy.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.ActivityMainBinding
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread(object : Runnable {
            override fun run() {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val threadName = Thread.currentThread().name

                Log.i("!!!", "responseCode:${connection.responseCode}")
                Log.i("!!!", "Выполняю запрос на потоке $threadName")
                Log.i("!!!", "responseMessage: ${connection.responseMessage}")

                val gson = Gson()
                val jsonString = connection.inputStream.bufferedReader().readText()
                val type = object : TypeToken<List<Category>>() {}.type
                val categories: List<Category>? = gson.fromJson(jsonString, type)

                val ids = categories?.map { it.id }
                ids?.forEach { id ->
                    threadPool.submit {
                        val recipeUrl =
                            URL("https://recipes.androidsprint.ru/api/category/$id/recipes")
                        val recipesConnection = recipeUrl.openConnection() as HttpURLConnection
                        recipesConnection.connect()
                        val recipesJson = recipesConnection.inputStream.bufferedReader().readText()
                        val recipesType = object : TypeToken<List<Recipe>>() {}.type
                        val recipes:List<Recipe> = Gson().fromJson(recipesJson, recipesType)

                        Log.i("recipes", "Результат вывода рецептов: $recipes")
                    }
                }

                Log.i("!!!!", "$categories")
            }
        })
        thread.name = "CheckingThread"
        thread.start()

        with(binding) {
            btScreenFavorites.setOnClickListener {
                setFavorites()
            }
            btCategory.setOnClickListener {
                setCategories()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setFavorites() {
        findNavController(R.id.nav_graph).navigate(R.id.favoritesFragment)
    }

    private fun setCategories() {
        findNavController(R.id.nav_graph).navigate(R.id.categoriesListFragment)
    }
}

