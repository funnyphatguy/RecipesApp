package ru.funny_phat_guy.recipesapp.di

import ru.funny_phat_guy.recipesapp.data.repo.RecipesRepository
import ru.funny_phat_guy.recipesapp.ui.recipes.favourites.FavoritesViewModel
import ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes.RecipesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}