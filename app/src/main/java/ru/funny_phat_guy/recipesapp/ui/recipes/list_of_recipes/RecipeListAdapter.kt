package ru.funny_phat_guy.recipesapp.ui.recipes.list_of_recipes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.databinding.ItemRecipeBinding
import ru.funny_phat_guy.recipesapp.model.Recipe

class RecipeListAdapter() :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<Recipe>()

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Recipe) {
            val drawable =
                AssetsImageLoader.loadImage(data.imageUrl, binding.cardImageBurgers.context)
            binding.cardImageBurgers.setImageDrawable(drawable)
            binding.cardTitle.text = data.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Recipe = dataSet[position]
        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(data.id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipeFromState(newDataSet: List<Recipe>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }
}
