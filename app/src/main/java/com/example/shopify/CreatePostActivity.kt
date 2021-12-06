package com.example.shopify

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shopify.daos.PostDao
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream

class CreatePostActivity : AppCompatActivity() {
    var imageUri: Uri? = null
    lateinit var imagePath: String
    lateinit var tags: String
    lateinit var storage: FirebaseStorage
    lateinit var storageRef: StorageReference
    lateinit var imageLabeler: ImageLabeler
    lateinit var inputImage: InputImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        val postDao = PostDao()
        supportActionBar?.hide()

        storage = FirebaseStorage.getInstance()

        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        storageRef = storage.reference
        postButton.setOnClickListener {
            val text = addText.text.toString().trim()
            val price = price.text.toString().trim()
            if (text.isNotEmpty()){
                postDao.addPost(text, imagePath, price, tags)
                finish()
            }
        }
        postImage.setOnClickListener {
            getImage()
        }
    }

    private fun getImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            imageUri = data.data
            Glide.with(applicationContext).load(imageUri).fitCenter().centerCrop().into(postImage)
            getTags(applicationContext, imageUri)
            uploadImage()
        }
    }

    private fun uploadImage() {
        progressBar.visibility = View.VISIBLE
        val postImagesRef = storageRef.child("images/"+System.currentTimeMillis())
        postImagesRef.putFile(imageUri!!).addOnSuccessListener {
            Handler(Looper.getMainLooper()).postDelayed({progressBar.visibility = View.GONE}, 500)
            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show()
            postImagesRef.downloadUrl.addOnSuccessListener {
                imagePath = it.toString()
            }
        }.addOnFailureListener{
            progressBar.visibility = View.GONE
            Toast.makeText(applicationContext, "Upload Failed", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener {
            val progressPercent = (100 * it.bytesTransferred/it.totalByteCount)
            progressBar.progress = progressPercent.toInt()
        }
    }
    private fun getTags(context: Context, imageUri: Uri?){
        val cr: ContentResolver = context.contentResolver
        val inp: InputStream? = cr.openInputStream(imageUri!!)
        val options = BitmapFactory.Options()
        options.inSampleSize = 8
        val image = BitmapFactory.decodeStream(inp, null, options)!!
        inputImage = InputImage.fromBitmap(image, 0)
        processImage()
    }

    private fun processImage(){
        imageLabeler.process(inputImage).addOnSuccessListener {
//            Handler(Looper.getMainLooper()).postDelayed({}, 500)
            tags = ""
            for(label in it){
                tags+= " ${label.text},"
            }
            Log.d("TAGS", tags)
        }.addOnFailureListener{

        }
    }
}