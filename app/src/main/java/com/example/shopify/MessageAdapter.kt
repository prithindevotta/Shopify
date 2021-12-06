package com.example.shopify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.models.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessageAdapter(private val listener: onClickMessage, options: FirestoreRecyclerOptions<User>) : FirestoreRecyclerAdapter<User, MessageAdapter.MessageViewHolder>(options) {

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userMessageImage)
        val userName: TextView = itemView.findViewById(R.id.userMessageName)
        val v: View = itemView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: User) {


        holder.userName.text = model.displayName
        Glide.with(holder.userImage.context).load(model.imageURl).circleCrop().into(holder.userImage)
        holder.v.setOnClickListener {
            val id = snapshots.getSnapshot(position).id
            listener.onItemClick(id, model.imageURl, model.displayName!!)
        }
    }
}