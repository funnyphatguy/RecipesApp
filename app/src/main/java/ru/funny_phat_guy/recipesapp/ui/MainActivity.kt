package ru.funny_phat_guy.recipesapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.ActivityMainBinding
import ru.funny_phat_guy.recipesapp.ui.categories.CategoriesListFragment
import ru.funny_phat_guy.recipesapp.ui.recipes.favourites.FavoritesFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
        findNavController(R.id.nav_graph).navigate(R.id.favoritesFragment2)
    }

    private fun setCategories() {
   findNavController(R.id.nav_graph).navigate(R.id.categoriesListFragment2)
    }
}

