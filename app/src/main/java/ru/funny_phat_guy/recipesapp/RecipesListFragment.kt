package ru.funny_phat_guy.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.funny_phat_guy.recipesapp.databinding.FragmentsListRecipesBinding


class RecipesListFragment : Fragment(R.layout.fragments_list_recipes) {
    private var _binding: FragmentsListRecipesBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding for FragmentRecipesBinding must not be null" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentsListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

}