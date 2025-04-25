package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.RecipesApplication
import ru.funny_phat_guy.recipesapp.databinding.FragmentRecipeBinding
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipeBinding must not be null" }

    private lateinit var recipeViewModel: RecipeViewModel

    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    class PortionSeekBarListener(
        private val onChangeIngredients: (Int) -> Unit
    ) : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val recipe = args.recipe
        initUI(recipe)
        initDivider()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var ingredientsAdapter: IngredientsAdapter =
        IngredientsAdapter()
    private var methodAdapter: MethodAdapter =
        MethodAdapter()

    private fun initUI(recipe: Recipe) {
        recipe.also { recipeViewModel.loadRecipe(it) }
        with(binding) {
            rvIngredients.adapter = ingredientsAdapter
            rvMethod.adapter = methodAdapter
            ivPreferences.setOnClickListener {
                recipeViewModel.onFavoritesClicked()
            }
            tvPortion.text = getString(R.string.portion_start)
            seekbar.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
                recipeViewModel.updatePortionCounter(
                    progress
                )
            })

            recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is RecipeViewModel.RecipeState.Loading -> {
                        Log.d("Categories", "Data loading in progress")
                    }

                    is RecipeViewModel.RecipeState.Content -> {
                        val ingredients = state.recipe?.ingredients ?: run {
                            return@observe
                        }
                        val method = state.recipe.method
                        val progressFromState = state.portionsCount
                        ingredientsAdapter.updateIngredientsFromState(ingredients)
                        ingredientsAdapter.updateIngredientsQuantity(progressFromState)
                        methodAdapter.getMethodFromState(method)
                        binding.tvPortion.text =
                            getString(R.string.portion_template, state.portionsCount)
                        tvRecipe.text = state.recipe.title
                        Glide.with(this@RecipeFragment)
                            .load("${Constants.BASE_IMAGES_URL}${state.recipeDrawable}")
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_error)
                            .into(ivRecipe)
                        ivPreferences.setImageResource(
                            if (state.isFavourites) R.drawable.ic_heart
                            else R.drawable.ic_heart_empty
                        )
                    }

                    is RecipeViewModel.RecipeState.Error -> {
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

    private fun initDivider() {
        val ingredientsRecyclerView = binding.rvIngredients
        val methodRecyclerView = binding.rvMethod
        val dividerItemDecoration = requireContext().let {
            MaterialDividerItemDecoration(it, DividerItemDecoration.VERTICAL).apply {
                isLastItemDecorated = false
            }
        }
        ingredientsRecyclerView.addItemDecoration(dividerItemDecoration)
        methodRecyclerView.addItemDecoration(dividerItemDecoration)
    }
}