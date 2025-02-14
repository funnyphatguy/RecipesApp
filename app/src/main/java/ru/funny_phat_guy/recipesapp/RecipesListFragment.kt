package ru.funny_phat_guy.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.funny_phat_guy.recipesapp.databinding.FragmentsListRecipesBinding
import ru.funny_phat_guy.recipesapp.models.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_ID
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_IMAGE_URL
import ru.funny_phat_guy.recipesapp.models.Constants.ARG_CATEGORY_NAME


class RecipesListFragment : Fragment(R.layout.fragments_list_recipes) {
    private var _binding: FragmentsListRecipesBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipesBinding must not be null" }

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentsListRecipesBinding.inflate(inflater, container, false)

        arguments?.let { args ->
            categoryId = args.getInt(ARG_CATEGORY_ID)
            categoryName = args.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawableBurgers = AssetsImageLoader.loadImage("burger.png",context)
        binding.recipeImageView.setImageDrawable(drawableBurgers)

    }

}