package com.example.shopify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.daos.MessageDao
import com.example.shopify.daos.PostDao
import com.example.shopify.models.Message
import com.example.shopify.models.Post
import com.example.shopify.models.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_description.*

class ChatAdapter(private val recId: String, options: FirestoreRecyclerOptions<Message>) : FirestoreRecyclerAdapter<Message, ChatAdapter.ChatViewHolder>(options) {

    lateinit var auth: FirebaseAuth
    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val senderMessage: TextView = itemView.findViewById(R.id.sendText)
        val receiverMessage: TextView = itemView.findViewById(R.id.recText)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Message) {
        auth = Firebase.auth
        val currId = auth.uid.toString()
        if (currId == model.senderUid && recId == model.receiverUid || currId == model.receiverUid && recId == model.senderUid){
            if(currId == model.senderUid){
                holder.receiverMessage.visibility = View.GONE
                holder.senderMessage.text = model.message
            }
            else if(currId == model.receiverUid){
                holder.senderMessage.visibility = View.GONE
                holder.receiverMessage.text = model.message
            }
        }
        else{
            holder.senderMessage.visibility = View.GONE
            holder.receiverMessage.visibility = View.GONE
        }

    }

}