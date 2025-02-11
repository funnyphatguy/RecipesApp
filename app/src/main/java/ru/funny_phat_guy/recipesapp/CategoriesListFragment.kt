package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.funny_phat_guy.recipesapp.databinding.FragmentListCategoriesBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.CategoriesListAdapter

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

        val drawable = AssetsImageLoader.loadImage("bcg_categories.png", context)
        binding.imageView.setImageDrawable(drawable)

    }

    fun openRecipesByCategoryId(){
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer)
            addToBackStack(null)
        }
    }

    fun initRecycler() {
        val categories = STUB.getCategories()
        val categoriesAdapter = CategoriesListAdapter(categories)
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick() {
                openRecipesByCategoryId()
            }
        })

    }
}

