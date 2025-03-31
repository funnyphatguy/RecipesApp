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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.ActivityMainBinding
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.model.Recipe
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
                val threadName = Thread.currentThread().name

                val interceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

                val request =
                    Request.Builder().url("https://recipes.androidsprint.ru/api/category").build()

                client.newCall(request).execute().use { response ->
                    val categoryBody = response.body?.string()
                    val categoryType = object : TypeToken<List<Category>>() {}.type

                    val categories = Gson().fromJson<List<Category>>(categoryBody, categoryType)

                    Log.i("!!!", "Тело $categories")

                    val ids = categories.map { it.id }
                    ids.forEach { id ->
                        threadPool.submit {
                            val recipeRequest = Request.Builder()
                                .url("https://recipes.androidsprint.ru/api/category/$id/recipes")
                                .build()
                            client.newCall(recipeRequest).execute().use { recipeResponse ->
                                val recipeBody = recipeResponse.body?.string()
                                val recipeType = object : TypeToken<List<Recipe>>() {}.type
                                val recipes: List<Recipe> = Gson().fromJson(recipeBody, recipeType)

                                Log.i("!!!", "Результат вывода рецептов: $recipes")
                            }
                        }
                    }
                }
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

