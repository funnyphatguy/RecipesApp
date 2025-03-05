package ru.funny_phat_guy.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.funny_phat_guy.recipesapp.databinding.ItemIngredientBinding
import ru.funny_phat_guy.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1


    class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Ingredient, quantity: Int) {
            with(binding) {
                tvMeasure.text = data.unitOfMeasure
                tvQuantity.text = data.quantity
                tvDescription.text = data.description

                val newQuantity = (BigDecimal(data.quantity) * BigDecimal(quantity))
                    .setScale(1, RoundingMode.HALF_UP)
                    .stripTrailingZeros()

                tvQuantity.text =
                    newQuantity.toInt().takeIf { newQuantity.scale() <= 0 }?.toString()
                        ?: newQuantity.toPlainString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemIngredientBinding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Ingredient = dataSet[position]
        holder.bind(data, quantity)


    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}