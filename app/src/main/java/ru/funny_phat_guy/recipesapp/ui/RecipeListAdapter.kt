package ru.funny_phat_guy.recipesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.funny_phat_guy.recipesapp.data.AssetsImageLoader
import ru.funny_phat_guy.recipesapp.databinding.ItemRecipeBinding
import ru.funny_phat_guy.recipesapp.model.Recipe


class RecipeListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
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

        holder.itemView.setOnClickListener{

            itemClickListener?.onItemClick(data.id)

        }
    }
}
