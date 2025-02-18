package ru.funny_phat_guy.recipesapp.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.funny_phat_guy.recipesapp.databinding.ItemIngridientBinding


class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ItemIngridientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Ingredient) {
            binding.tvMeasure.text = data.unitOfMeasure
            binding.tvQuantity.text = data.quantity
            binding.tvDescription.text = data.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemIngridientBinding =
            ItemIngridientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Ingredient = dataSet[position]
        holder.bind(data)
    }
}