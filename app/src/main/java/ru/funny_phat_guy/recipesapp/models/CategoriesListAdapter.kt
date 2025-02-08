package ru.funny_phat_guy.recipesapp.models

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.funny_phat_guy.recipesapp.databinding.ItemCategoryBinding
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Category) {
            val inputStream: InputStream? = binding.root.context.assets?.open(data.imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.cardImage.setImageDrawable(drawable)
            binding.cardTitle.text = data.title
            binding.cardDescription.text = data.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data: Category = dataSet[position]
        viewHolder.bind(data)

    }
    override fun getItemCount() = dataSet.size

}