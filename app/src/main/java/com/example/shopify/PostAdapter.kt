package com.example.shopify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PostAdapter(private val listener: onClick, options: FirestoreRecyclerOptions<Post>) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val userName: TextView = itemView.findViewById(R.id.userNameChat)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val postTitle: TextView = itemView.findViewById(R.id.postTitle)
        val price: TextView = itemView.findViewById(R.id.price)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        val v: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.userName.text = model.createdBy.displayName
        holder.postTitle.text = model.text
        ("Rs. "+model.price).also { holder.price.text = it }
        Glide.with(holder.userImage.context).load(model.createdBy.imageURl).circleCrop().into(holder.userImage)
        Glide.with(holder.postImage.context).load(model.image).fitCenter().centerCrop().into(holder.postImage)
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.v.setOnClickListener {
            val id = snapshots.getSnapshot(position).id
            listener.onItemClick(id)
        }

    }

}