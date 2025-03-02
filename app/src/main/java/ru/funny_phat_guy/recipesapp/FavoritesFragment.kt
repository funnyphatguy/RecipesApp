package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.funny_phat_guy.recipesapp.databinding.FragmentFavoritesBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favouritesPicture = AssetsImageLoader.loadImage("bcg_favorites.png",context)
        binding.ivFavourites.setImageDrawable(favouritesPicture)
        binding.tvFavourites.text = getString(R.string.recipe_favourites_category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}