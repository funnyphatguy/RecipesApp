package ru.funny_phat_guy.recipesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.funny_phat_guy.recipesapp.databinding.FragmentRecipeBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_PREFERENCES
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_RECIPE
import ru.funny_phat_guy.recipesapp.models.Constants.FAVOURITES
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

        initUI(recipe)

        initRecycler(recipe)

        initDivider()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val sharedPref by lazy {
        requireContext().getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun saveFavorites(ides: Set<String>) {
        sharedPref.edit().putStringSet(FAVOURITES, ides).apply()
    }

    private fun getFavourites(): HashSet<String> {
        val favoriteSet = sharedPref.getStringSet(FAVOURITES, emptySet()).orEmpty()
        return HashSet(favoriteSet)
    }

    private fun initUI(recipe: Recipe?) {
        binding.tvRecipe.text = recipe?.title
        val drawableTitle = recipe?.imageUrl?.let { AssetsImageLoader.loadImage(it, context) }
        binding.ivRecipe.setImageDrawable(drawableTitle)
        binding.tvPortion.text = getString(R.string.portion_start)
        binding.ivFavourites.setImageResource(R.drawable.ic_heart_empty)
        val currentRecipeId = recipe?.id?.toString() ?: return

        val ides = getFavourites()

        binding.ivFavourites.setImageResource(
            if (ides.contains(currentRecipeId)) R.drawable.ic_heart
            else R.drawable.ic_heart_empty
        )

        binding.ivFavourites.setOnClickListener {
            if (ides.contains(currentRecipeId)) {
                ides.remove(currentRecipeId)
                binding.ivFavourites.setImageResource(R.drawable.ic_heart_empty)
            } else {
                ides.add(currentRecipeId)
                binding.ivFavourites.setImageResource(R.drawable.ic_heart)
            }
            saveFavorites(ides)
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