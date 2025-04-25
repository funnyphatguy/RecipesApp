package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.RecipesApplication
import ru.funny_phat_guy.recipesapp.databinding.FragmentsListRecipesBinding
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.ui.Constants
import ru.funny_phat_guy.recipesapp.ui.categories.CategoriesViewModel
import kotlin.getValue

@AndroidEntryPoint
class RecipesListFragment : Fragment() {
    private var _binding: FragmentsListRecipesBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipesBinding must not be null" }

    private val recipesViewModel: RecipesViewModel by viewModels()
    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    private fun initUI(category: Category) {
        recipesViewModel.loadRecipesData(category.id, category.imageUrl, category.title)

        recipesViewModel.allRecipeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RecipesViewModel.ListOfRecipeState.Loading -> {
                    Log.d("Recipes", "Data loading in progress")
                }

                is RecipesViewModel.ListOfRecipeState.Content -> {

                    Glide.with(this)
                        .load("${Constants.BASE_IMAGES_URL}${state.categoryPictureUrl}")
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(binding.ivRecipe)
                    recipeAdapter.updateRecipeFromState(state.recipes)
                    binding.rvRecipes.adapter = recipeAdapter
                    binding.tvRecipe.text = state.categoryDescription
                    recipeAdapter.setOnItemClickListener(object :
                        RecipeListAdapter.OnItemClickListener {
                        override fun onItemClick(recipeId: Int) {
                            openRecipeByRecipeId(recipeId)
                        }
                    })
                }

                is RecipesViewModel.ListOfRecipeState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        recipesViewModel.allRecipeState.value?.let { currentState ->
            when (currentState) {
                is RecipesViewModel.ListOfRecipeState.Loading -> {
                    Log.i("RecipesListFragment", "RecipesListFragment is loading")
                }

                is RecipesViewModel.ListOfRecipeState.Content -> {
                    val recipe = currentState.recipes.find { it.id == recipeId } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "Рецепт не найден",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    val action =
                        RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                            recipe = recipe
                        )
                    findNavController().navigate(action)
                }

                is RecipesViewModel.ListOfRecipeState.Error ->
                    Toast.makeText(
                        requireContext(),
                        "Ошибка загрузки данных: ${currentState.message}",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }


}
