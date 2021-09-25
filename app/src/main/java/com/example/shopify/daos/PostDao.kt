package com.example.shopify.daos

import android.provider.Settings
import com.example.shopify.models.Post
import com.example.shopify.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("post")
    val auth = Firebase.auth

    fun addPost(text: String){
        val currentId = auth.currentUser!!.uid
        GlobalScope.launch(Dispatchers.IO){
            val userDao = UserDao()
            val user = userDao.getUserById(currentId).await().toObject(User::class.java)!!
            val time = System.currentTimeMillis()
            val post = Post(text, user, time)
            postCollection.document().set(post)
        }
    }
}