package com.example.shopify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.shopify.daos.MessageDao
import com.example.shopify.models.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_messaging.*
import kotlin.check

class ChatActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var message: String
    private lateinit var adapter: ChatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar!!.hide()
        val bundle = intent.extras!!
        val id = bundle.getString("id")
        val userName = bundle.getString("user")
        val imageUrl = bundle.getString("url")

        auth = Firebase.auth
        val currId = auth.currentUser!!.uid
        val messageDao = MessageDao()
        val messageCollection = messageDao.messageCollection
        backButton.setOnClickListener {
            val intent = Intent(this, MessagingActivity::class.java)
            startActivity(intent)
        }
        userNameChat.text = userName
        Glide.with(this).load(imageUrl).circleCrop().into(recImage)

        val query = messageCollection
        setRecyclerView(id.toString(), query)

        senderButton.setOnClickListener {
            message = senderText.text.toString()
            if (message.isEmpty()){
                Log.e("chatActivity", "type something bitch")
            }
            else{
                senderText.text.clear()
                messageDao.addMessage(message, "text", currId, id.toString())
            }
        }
    }

    private fun setRecyclerView(id: String, query: com.google.firebase.firestore.Query) {
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<com.example.shopify.models.Message>().setQuery(query, com.example.shopify.models.Message::class.java).build()
        adapter = ChatAdapter(id, recyclerViewOptions)
        recyclerViewChat.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true;
        recyclerViewChat.layoutManager = layoutManager
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}