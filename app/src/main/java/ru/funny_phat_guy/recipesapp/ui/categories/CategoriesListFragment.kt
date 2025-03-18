package ru.funny_phat_guy.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.databinding.FragmentListCategoriesBinding
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_CATEGORY_ID
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_CATEGORY_IMAGE_URL
import ru.funny_phat_guy.recipesapp.ui.Constants.ARG_CATEGORY_NAME
import ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = requireNotNull(_binding) { "Binding for FragmentCategoriesBinding must not be null" }

    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val categoriesAdapter = CategoriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
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

    private fun openRecipesByCategoryId(categoryId: Int) {
        val categories = categoriesViewModel.getCategoryById(categoryId) ?: run {
            Toast.makeText(context, "Category not found", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryName: String = categories.title
        val categoryImageUrl: String = categories.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl,
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }

    private fun initUI() {
        val drawableCategories = AssetsImageLoader.loadImage("bcg_categories.png", context)
        binding.imageView.setImageDrawable(drawableCategories)

        categoriesViewModel.allCategoryState.observe(viewLifecycleOwner){state->
            val categories = state.categories
            categories?.let { categoriesAdapter.updateCategoryFromState(categories) }
            binding.rvCategories.adapter = categoriesAdapter

            categoriesAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })
        }
    }
}

