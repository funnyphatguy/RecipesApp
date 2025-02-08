package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.funny_phat_guy.recipesapp.databinding.FragmentListCategoriesBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.CategoriesListAdapter

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = requireNotNull(_binding) { "Binding for FragmentFavoritesBinding must not be null" }


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

        val imageName = "bcg_categories.png"
        (requireContext())
        val bitmap = AssetsImageLoader.loadImage(requireContext(), imageName)

        if (bitmap != null) {
            binding.imageView.setImageBitmap(bitmap)
        } else {
            Log.e("ListCategoriesFragment", "Failed to load image $imageName")
        }
    }


    fun initRecycler() {
        val categories = STUB.getCategories()
        val categotiesAdapter = CategoriesListAdapter(categories)
        binding.rvCategories.layoutManager = GridLayoutManager(context, 2)
        binding.rvCategories.adapter = categotiesAdapter
    }
}

