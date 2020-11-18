package com.example.fakeapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_main.view.*

class PostsAdapter(
    private val posts : List<Post>,
    private val onClick : (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    class PostViewHolder(val root : View) : RecyclerView.ViewHolder(root){
        fun bind(post : Post){
            with(root){
                title.text = post.title
                body.text = post.body
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val holder = PostViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.post_main, parent, false)
        )
        holder.root.button.setOnClickListener {
            onClick(posts[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount() = this.posts.size
    override fun onBindViewHolder(holder: PostsAdapter.PostViewHolder, position: Int) = holder.bind(posts[position])

}