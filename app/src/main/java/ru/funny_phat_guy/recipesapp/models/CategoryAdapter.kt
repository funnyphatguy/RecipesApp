package ru.funny_phat_guy.recipesapp.models

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.funny_phat_guy.recipesapp.R
import ru.funny_phat_guy.recipesapp.databinding.ItemCategoryBinding
import java.io.InputStream
import kotlin.contracts.Returns

class CategoryAdapter(private val dataSet: Array<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val cardImageView: ImageView
//        val cardTitleTextView: TextView
//        val cardDescriptionView: TextView
//
//
//        init {
//            // Define click listener for the ViewHolder's View
//
//            cardImageView = view.findViewById(R.id.card_image)
//            cardTitleTextView = view.findViewById(R.id.card_title)
//            cardDescriptionView = view.findViewById(R.id.card_description)
//        }
//    }

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Category) {
            Glide.with(itemView).load(data.imageUrl).into(binding.cardImage)
            binding.cardTitle.text = data.title
            binding.cardDescription.text = data.description
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val data = dataSet[position]
        viewHolder.bind(data)

        val context = viewHolder.itemView.context
        val imageName = "bcg_categories.png"
        val bitmap = AssetsImageLoader.loadImage(context, imageName)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}