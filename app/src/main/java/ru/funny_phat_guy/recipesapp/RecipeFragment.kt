package ru.funny_phat_guy.recipesapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.funny_phat_guy.recipesapp.databinding.FragmentRecipeBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_RECIPE
import ru.funny_phat_guy.recipesapp.models.IngredientsAdapter
import ru.funny_phat_guy.recipesapp.models.MethodAdapter
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
        binding.recipeTextView.text = recipe?.title
        val ingredients = recipe?.ingredients ?: run {
            Toast.makeText(context, "Ingredient not found", Toast.LENGTH_SHORT).show()
            return
        }



        val ingredientsAdapter = IngredientsAdapter(ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter

        val drawableTitle = AssetsImageLoader.loadImage(recipe.imageUrl, context)
        binding.recipeImageView.setImageDrawable(drawableTitle)

        val method = recipe.method
        val methodAdapter = MethodAdapter(method)
        binding.rvMethod.adapter = methodAdapter

        val ingredientsRecyclerView = binding.rvIngredients
        val methodRecyclerView = binding.rvMethod
        val dividerItemDecoration =
            context?.let { MaterialDividerItemDecoration(it, DividerItemDecoration.VERTICAL) }
        if (dividerItemDecoration != null) {
            ingredientsRecyclerView.addItemDecoration(dividerItemDecoration)
            methodRecyclerView.addItemDecoration(dividerItemDecoration)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}