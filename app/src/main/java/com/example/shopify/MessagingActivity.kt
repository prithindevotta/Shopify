package com.example.shopify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopify.daos.UserDao
import com.example.shopify.models.Post
import com.example.shopify.models.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_messaging.*

class MessagingActivity : AppCompatActivity(),  onClickMessage{
    lateinit var adapter:MessageAdapter
    lateinit var userDao:UserDao
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)
        supportActionBar?.hide()

        userDao = UserDao()
        val userCollection = userDao.getUserCollection()
        auth = Firebase.auth
        val currUser = auth.currentUser!!.uid
        val displayName = auth.currentUser!!.displayName
        val query = userCollection.orderBy("uid").whereNotEqualTo("uid", currUser)
        setRecyclerView(query)
        search_bar_chat.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val query:Query
                if(s.toString() !=""){
                    query = userCollection.whereNotEqualTo("displayName", displayName).whereGreaterThanOrEqualTo("displayName", s.toString()).whereLessThanOrEqualTo("displayName", s.toString()+"\uf8ff")
                    val recyclerViewOptions = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java).build()
                    adapter.updateOptions(recyclerViewOptions)
                }
                else{
                    query = userCollection.orderBy("uid").whereNotEqualTo("uid", currUser)
                    val recyclerViewOptions = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java).build()
                    adapter.updateOptions(recyclerViewOptions)
                }
            }

        })
    }
    private fun setRecyclerView(query: Query) {
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java).build()
        adapter = MessageAdapter(this, recyclerViewOptions)
        recyclerViewMssg.adapter = adapter
        recyclerViewMssg.layoutManager = LinearLayoutManager(this)
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onItemClick(id: String, url: String, userName: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("url", url)
        intent.putExtra("user", userName)
        startActivity(intent)
    }

}

interface onClickMessage {
    fun onItemClick(id: String, url: String, userName: String)
}
