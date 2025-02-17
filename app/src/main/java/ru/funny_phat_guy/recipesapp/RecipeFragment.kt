package ru.funny_phat_guy.recipesapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.funny_phat_guy.recipesapp.databinding.FragmentRecipeBinding
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_RECIPE
import ru.funny_phat_guy.recipesapp.models.Recipe

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipeBinding must not be null" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE)
        }
        binding.recipeTxt.text = recipe?.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}