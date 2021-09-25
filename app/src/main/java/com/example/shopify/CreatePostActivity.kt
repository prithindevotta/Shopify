package com.example.shopify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopify.daos.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        val postDao = PostDao()

        postButton.setOnClickListener {
            val text = addText.text.toString().trim()
            if (text.isNotEmpty()){
                postDao.addPost(text)
                finish()
            }
        }
    }
}