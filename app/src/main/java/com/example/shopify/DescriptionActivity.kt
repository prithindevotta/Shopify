package com.example.shopify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.shopify.daos.PostDao
import com.example.shopify.models.Post
import kotlinx.android.synthetic.main.activity_description.*


class DescriptionActivity : AppCompatActivity() {

    private val TAG = "Error"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        supportActionBar?.hide()
        val id = intent.extras!!.get("item")
        val postDao = PostDao()
        val postCollections = postDao.postCollection
        postCollections.document(id.toString()).get().addOnSuccessListener {
            val post = it.toObject(Post::class.java)!!
            Glide.with(this).load(post.createdBy.imageURl).circleCrop().into(userImageDesc)
            userNameDesc.text = post.createdBy.displayName.toString()
            createdAt.text = Utils.getTimeAgo(post.createdAt)
            Glide.with(this).load(post.image).into(postImageDes)
            description.text = post.text
            ("Rs. "+post.price).also {price.text = it }
            ("Tags: ${post.tags}").also{ tags.text = it }
        }
    }
}