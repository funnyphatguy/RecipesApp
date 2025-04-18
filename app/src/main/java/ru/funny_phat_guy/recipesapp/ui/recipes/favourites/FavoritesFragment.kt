package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.FragmentFavoritesBinding
import ru.funny_phat_guy.recipesapp.ui.Constants
import ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes.RecipeListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val favoritesAdapter = RecipeListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        favoritesViewModel.favoritesRecipeState.value?.let { currentState ->
            when (currentState) {
                is FavoritesViewModel.FavoritesState.Loading -> {
                    Log.i("RecipesListFragment", "RecipesListFragment is loading")
                }

                is FavoritesViewModel.FavoritesState.Success -> {
                    val recipe = currentState.recipes.find { it.id == recipeId } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "Рецепт не найден",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    val action =
                        FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                            recipe = recipe
                        )
                    findNavController().navigate(action)
                }

                is FavoritesViewModel.FavoritesState.Error ->
                    Toast.makeText(
                        requireContext(),
                        "Ошибка загрузки данных: ${currentState.message}",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun initUI() {
        Glide.with(this)
            .load(Constants.BCQ_FAVORITES_PATH)
            .placeholder(R.drawable.img_placeholder)
            .into(binding.ivFavorites)

        binding.tvFavorites.text = getString(R.string.recipe_favorites_category)

        favoritesViewModel.loadFavorites()

        favoritesViewModel.favoritesRecipeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesViewModel.FavoritesState.Loading -> {
                    Log.d("Favorites", "Data loading in progress")
                }

                is FavoritesViewModel.FavoritesState.Success -> {
                    if (state.recipes.isEmpty()) {
                        binding.tvNothing.text = getString(R.string.empty_favorites)
                        binding.tvNothing.visibility = View.VISIBLE
                        binding.rvFavorites.visibility = View.GONE
                    } else {
                        binding.tvNothing.visibility = View.GONE
                        binding.rvFavorites.visibility = View.VISIBLE
                        favoritesAdapter.updateRecipeFromState(state.recipes)
                        binding.rvFavorites.adapter = favoritesAdapter
                        favoritesAdapter.setOnItemClickListener(object :
                            RecipeListAdapter.OnItemClickListener {
                            override fun onItemClick(recipeId: Int) {
                                openRecipeByRecipeId(recipeId)
                            }
                        })
                    }
                }

                is FavoritesViewModel.FavoritesState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}