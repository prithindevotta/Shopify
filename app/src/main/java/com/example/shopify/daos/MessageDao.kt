package com.example.shopify.daos

import com.example.shopify.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MessageDao {
    val db = FirebaseFirestore.getInstance()
    val messageCollection = db.collection("message")

    fun addMessage(message: String, type:String, senderUid:String, receiverUid: String){
        GlobalScope.launch(Dispatchers.IO){
            val time = System.currentTimeMillis().toString()
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val formatedDate = formatter.format(date)
            val messageObj = Message(message, time, formatedDate, type, senderUid, receiverUid)
            messageCollection.document(time).set(messageObj)
        }
    }
}