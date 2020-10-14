package com.example.networking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_view.view.*

class ImagesAdapter(
    private val images : List<Image>,
    private val onClick : (Image) -> Unit
): RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>(){
    class ImageViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(image: Image){
            with(root){
                Id.text = image.id.toString()
                Author.text = image.author
                Url.text = image.url
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val holder = ImageViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.image_view, parent, false)
        )
        holder.root.setOnClickListener{
            onClick(images[holder.adapterPosition])}
        return holder
        }

    override fun getItemCount(): Int {
        return images.size
    }
    override fun onBindViewHolder(holder: ImagesAdapter.ImageViewHolder, position: Int) = holder.bind(images[position])
}
