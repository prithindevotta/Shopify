package com.example.shopify.daos

import com.example.shopify.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun addUsers(users: User?){
        users?.let {
            GlobalScope.launch(Dispatchers.IO){
                userCollection.document(users.uid).set(it)
            }
        }
    }
    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }
}