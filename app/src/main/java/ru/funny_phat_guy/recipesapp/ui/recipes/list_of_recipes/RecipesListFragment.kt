package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.funny_phat_guy.recipesapp.databinding.FragmentsListRecipesBinding
import ru.funny_phat_guy.recipesapp.model.Category

class RecipesListFragment : Fragment() {
    private var _binding: FragmentsListRecipesBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipesBinding must not be null" }

    private val recipesViewModel: RecipesViewModel by viewModels()
     private val args:RecipesListFragmentArgs by navArgs()

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
        val categoryFromCategories = args.category
        initUI(categoryFromCategories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val recipeAdapter: RecipeListAdapter = RecipeListAdapter()

    private fun initUI(category:Category) {
        recipesViewModel.loadRecipesData(category.id, category.imageUrl, category.title)
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
        val action = RecipesListFragmentDirections.
        actionRecipesListFragmentToRecipeFragment(
            recipeId = recipeId
        )
        findNavController().navigate(action)
    }
}