package ru.funny_phat_guy.recipesapp.ui.categories

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.ItemCategoryBinding
import ru.funny_phat_guy.recipesapp.model.Category
import ru.funny_phat_guy.recipesapp.ui.Constants

class CategoriesListAdapter() :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<Category>()

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {

        itemClickListener = listener

    }

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Category, holder: ViewHolder) {
            Glide.with(holder.itemView).load("${Constants.BASE_IMAGES_URL}${data.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.cardImage)
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
        viewHolder.bind(data, viewHolder)

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(data.id)
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCategoryFromState(newDataSet: List<Category>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

}