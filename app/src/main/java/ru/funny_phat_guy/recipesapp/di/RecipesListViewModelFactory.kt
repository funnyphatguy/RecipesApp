package ru.funny_phat_guy.recipesapp.di

import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes.RecipesViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipesViewModel> {
    override fun create(): RecipesViewModel {
        return RecipesViewModel(recipesRepository)
    }
}