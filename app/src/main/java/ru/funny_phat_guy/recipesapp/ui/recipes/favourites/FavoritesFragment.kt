package ru.funny_phat_guy.recipesapp.ui.recipes.favourites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.data.STUB
import ru.funny_phat_guy.recipesapp.databinding.FragmentFavoritesBinding
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_RECIPE_ID
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES
import ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes.RecipeListAdapter
import ru.funny_phat_guy.recipesapp.ui.recipes.recipe.RecipeFragment

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoritesPicture = AssetsImageLoader.loadImage("bcg_favorites.png", context)
        binding.ivFavorites.setImageDrawable(favoritesPicture)
        binding.tvFavorites.text = getString(R.string.recipe_favorites_category)
        if (getFavoritesFragment().isEmpty()) {
            binding.tvNothing.text = getString(R.string.empty_favorites)
            binding.rvFavorites.visibility = View.GONE
        }
        else
            initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }

    private fun getFavoritesFragment(): Set<Int> {
        val sharedPreferences by lazy {
            requireContext().getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
        }
        val favoriteSet = sharedPreferences.getStringSet(FAVORITES, emptySet()).orEmpty()

        val favoriteSetInt = favoriteSet.mapNotNull { it.toIntOrNull() }.toSet()
        return favoriteSetInt
    }

    private fun initRecycler() {
        val recipes = STUB.getRecipesByIds(getFavoritesFragment())
//        val recipesAdapter = RecipeListAdapter(recipes)
//        binding.rvFavorites.adapter = recipesAdapter
//
//        recipesAdapter.setOnItemClickListener(object :
//            RecipeListAdapter.OnItemClickListener {
//            override fun onItemClick(recipeId: Int) {
//                openRecipeByRecipeId(recipeId)
//            }
//        })
    }
}
