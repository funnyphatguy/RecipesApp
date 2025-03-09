package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.databinding.FragmentRecipeBinding
import ru.funny_phat_guy.recipesapp.model.Recipe
import ru.funny_phat_guy.recipesapp.ui.Constants
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_RECIPE_ID
import ru.funny_phat_guy.recipesapp.ui.Constants.FAVORITES

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt(ARG_RECIPE_ID)?.let { recipeViewModel.loadRecipe(it) }

        recipeViewModel.recipeLiveData.observe(viewLifecycleOwner) { state ->
            state.let {
                Log.i(
                    Constants.LOG_INFO_TAG,
                    "isFavorite: ${it.isFavourites}"
                )
            }
        }


        initUI()

//        initRecycler(recipeId)

        initDivider()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val sharedPref: SharedPreferences by lazy {
        requireContext().getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun saveFavorites(ides: Set<String>) {
        sharedPref.edit().putStringSet(FAVORITES, ides).apply()
    }

    private fun initUI() {

        binding.tvPortion.text = getString(R.string.portion_start)

        recipeViewModel.recipeLiveData.observe(viewLifecycleOwner) { state ->
            state.let {
                binding.tvRecipe.text = state.recipe?.title
                val drawableTitle = state.recipe?.imageUrl.let {
                    AssetsImageLoader.loadImage(
                        it.toString(),
                        context
                    )
                }
                binding.ivRecipe.setImageDrawable(drawableTitle)

                binding.ivPreferences.setImageResource(
                    if (state.isFavourites) R.drawable.ic_heart
                    else R.drawable.ic_heart_empty
                )

                binding.ivPreferences.setOnClickListener {
//                    if (ides.contains(currentRecipeId)) {
//                        ides.remove(currentRecipeId)
//                        binding.ivPreferences.setImageResource(R.drawable.ic_heart_empty)
//                    } else {
//                        ides.add(currentRecipeId)
//                        binding.ivPreferences.setImageResource(R.drawable.ic_heart)
//                    }
//                    saveFavorites(ides)
                    recipeViewModel.onFavoritesClicked()
                }
            }
        }
    }

    private fun initRecycler(recipe: Recipe?) {
        val ingredients = recipe?.ingredients ?: run {
            Toast.makeText(context, "Ingredient not found", Toast.LENGTH_SHORT).show()
            return
        }

        val method = recipe.method

        val ingredientsAdapter = IngredientsAdapter(ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter

        val methodAdapter = MethodAdapter(method)
        binding.rvMethod.adapter = methodAdapter

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val portionsText = getString(R.string.portion_template, progress)
                binding.tvPortion.text = portionsText
                ingredientsAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun initDivider() {
        val ingredientsRecyclerView = binding.rvIngredients
        val methodRecyclerView = binding.rvMethod

        val dividerItemDecoration = context?.let {
            MaterialDividerItemDecoration(it, DividerItemDecoration.VERTICAL).apply {
                isLastItemDecorated = false
            }
        }

        if (dividerItemDecoration != null) {
            ingredientsRecyclerView.addItemDecoration(dividerItemDecoration)
            methodRecyclerView.addItemDecoration(dividerItemDecoration)
        }
    }
}