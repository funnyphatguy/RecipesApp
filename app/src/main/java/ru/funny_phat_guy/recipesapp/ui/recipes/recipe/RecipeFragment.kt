package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.FragmentRecipeBinding
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_RECIPE_ID

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipeBinding must not be null" }

    private val recipeViewModel: RecipeViewModel by viewModels()

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
        initUI()
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

    private fun initUI() {

        val recipeId = arguments?.getInt(ARG_RECIPE_ID)
        recipeId?.also { recipeViewModel.loadRecipe(it) }

        binding.rvIngredients.adapter = ingredientsAdapter

        binding.rvMethod.adapter = methodAdapter

        with(binding) {
            ivPreferences.setOnClickListener {
                recipeViewModel.onFavoritesClicked()
            }
            tvPortion.text = getString(R.string.portion_start)
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                recipeViewModel.updatePortionCounter(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            with(binding) {

                val ingredients = state.recipe?.ingredients ?: run {
                    Toast.makeText(requireContext(), "Ingredient not found", Toast.LENGTH_SHORT)
                        .show()
                    return@observe
                }
                val method = state.recipe.method
                val progressFromState = state.portionsCount

                ingredientsAdapter.updateIngredients(progressFromState)

                ingredientsAdapter.getIngredientsFromState(ingredients)

                methodAdapter.getMethodFromState(method)

                binding.tvPortion.text = getString(R.string.portion_template, state.portionsCount)

                tvRecipe.text = state.recipe.title

                ivRecipe.setImageDrawable(state.recipeImage)

                ivPreferences.setImageResource(
                    if (state.isFavourites) R.drawable.ic_heart
                    else R.drawable.ic_heart_empty
                )

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