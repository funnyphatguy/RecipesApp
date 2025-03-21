package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.FragmentsListRecipesBinding
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_CATEGORY_ID
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_CATEGORY_IMAGE_URL
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_CATEGORY_NAME
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_RECIPE_ID
import ru.funny_phat_guy.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentsListRecipesBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipesBinding must not be null" }

    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentsListRecipesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val recipeAdapter: RecipeListAdapter = RecipeListAdapter()

    private fun initUI() {

        arguments?.let { args ->
            val categoryId = args.getInt(ARG_CATEGORY_ID)
            val categoryName = args.getString(ARG_CATEGORY_NAME)
            val categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
            recipesViewModel.loadRecipesData(categoryId, categoryImageUrl, categoryName)
        }

        binding.rvRecipes.adapter = recipeAdapter

        recipesViewModel.allRecipeState.observe(viewLifecycleOwner) { state ->
            val recipes = state.recipes
            recipes?.let { recipeAdapter.updateRecipeFromState(it) }
            val categoryImage = state.categoryImage
            val description = state.categoryDescription

            binding.ivRecipe.setImageDrawable(categoryImage)
            binding.tvRecipe.text = description

            recipeAdapter.setOnItemClickListener(object :
                RecipeListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = recipesViewModel.takeRecipeId(recipeId)
        val bundle = bundleOf(ARG_RECIPE_ID to recipe?.id)
        findNavController().navigate(R.id.recipeFragment2, args = bundle)
    }
}