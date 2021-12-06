package com.example.shopify.models


class Post(val text: String = "",
           val image: String? = "",
           val price: String = "",
           val createdBy: User = User(),
           val createdAt: Long = 0L,
           val tags: String = "") {

}