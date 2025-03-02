package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.funny_phat_guy.recipesapp.databinding.FragmentsListRecipesBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_ID
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_IMAGE_URL
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_NAME
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_RECIPE
import ru.funny_phat_guy.recipesapp.models.RecipeListAdapter

class RecipesListFragment : Fragment() {
    private var _binding: FragmentsListRecipesBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipesBinding must not be null" }

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentsListRecipesBinding.inflate(inflater, container, false)

        arguments?.let { args ->
            categoryId = args.getInt(ARG_CATEGORY_ID)
            categoryName = args.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        val drawableBurgers = AssetsImageLoader.loadImage(categoryImageUrl.toString(), context)
        binding.ivRecipe.setImageDrawable(drawableBurgers)
        binding.tvRecipe.text = categoryName

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

     fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)

        val bundle = bundleOf(ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }

    private fun initRecycler() {
        val recipes = STUB.getRecipesByCategoryId(categoryId)
        val recipesAdapter = RecipeListAdapter(recipes)
        binding.rvRecipes.adapter = recipesAdapter

        recipesAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

}