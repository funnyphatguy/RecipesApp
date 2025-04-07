package ru.funny_phat_guy.recipesapp.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.FragmentListCategoriesBinding
import ru.funny_phat_guy.recipesapp.ui.Constants

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
        val currentState = categoriesViewModel.allCategoryState.value

        when (currentState) {
            is CategoriesViewModel.CategoriesState.Success -> {
                val category = currentState.categories.find { it.id == categoryId } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "Категория не найдена",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@openRecipesByCategoryId
                }

                val action = CategoriesListFragmentDirections
                    .actionCategoriesListFragmentToRecipesListFragment(category)
                findNavController().navigate(action)
            }

            is CategoriesViewModel.CategoriesState.Error -> {
                Toast.makeText(
                    requireContext(),
                    "Ошибка загрузки данных: ${currentState.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            CategoriesViewModel.CategoriesState.Loading -> {
                Toast.makeText(
                    requireContext(),
                    "Данные загружаются, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Toast.makeText(requireContext(), "Данные не готовы", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initUI() {
        Glide.with(this)
            .load(Constants.BCQ_CATEGORIES_PATH)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivCategories)

        categoriesViewModel.allCategoryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CategoriesViewModel.CategoriesState.Loading -> {
                    Log.d("Categories", "Data loading in progress")
                }

                is CategoriesViewModel.CategoriesState.Success -> {
                    categoriesAdapter.updateCategoryFromState(state.categories)
                    binding.rvCategories.adapter = categoriesAdapter
                    categoriesAdapter.setOnItemClickListener(object :
                        CategoriesListAdapter.OnItemClickListener {
                        override fun onItemClick(categoryId: Int) {
                            openRecipesByCategoryId(categoryId)
                        }
                    })
                }

                is CategoriesViewModel.CategoriesState.Error -> {
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

