package ru.funny_phat_guy.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.databinding.FragmentListCategoriesBinding

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
        val category = categoriesViewModel.getCategoryById(categoryId) ?:
        throw IllegalArgumentException("Category not found")

        val action = CategoriesListFragmentDirections.
        actionCategoriesListFragmentToRecipesListFragment(
            category
        )
        findNavController().navigate(action)
    }

        private fun initUI() {
            val drawableCategories = AssetsImageLoader.loadImage("bcg_categories.png", context)
            binding.imageView.setImageDrawable(drawableCategories)

            categoriesViewModel.getCategories()

            categoriesViewModel.allCategoryState.observe(viewLifecycleOwner) { state ->
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

