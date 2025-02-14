package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.funny_phat_guy.recipesapp.databinding.FragmentListCategoriesBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.CategoriesListAdapter
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_ID
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_IMAGE_URL
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_NAME

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = requireNotNull(_binding) { "Binding for FragmentCategoriesBinding must not be null" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        val drawableCategories = AssetsImageLoader.loadImage("bcg_categories.png", context)
        binding.imageView.setImageDrawable(drawableCategories)

    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val categories = STUB.getCategories().find { it.id == categoryId } ?: run {
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

    private fun initRecycler() {
        val categories = STUB.getCategories()
        val categoriesAdapter = CategoriesListAdapter(categories)
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }
}

