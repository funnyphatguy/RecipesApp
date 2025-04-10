package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.funny_phat_guy.recipesapp.databinding.ItemMethodBinding

class MethodAdapter() : RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<String>()

    class ViewHolder(
        private val binding: ItemMethodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.txtMethod.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: String = dataSet[position]
        val numberedData = "${position + 1}. $data"
        holder.bind(numberedData)
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun getMethodFromState(newItems: List<String>) {
        dataSet.clear()
        dataSet.addAll(newItems)
        notifyDataSetChanged()
    }
}