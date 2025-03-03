package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.funny_phat_guy.recipesapp.databinding.ActivityMainBinding

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

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(R.id.mainContainer)
            }
        }

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
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FavoritesFragment>(R.id.mainContainer)
            addToBackStack(null)
        }
    }

    private fun setCategories() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<CategoriesListFragment>(R.id.mainContainer)
            addToBackStack(null)
        }
    }
}

