package com.example.bluetoothdemo.data.model

/**
 * Post 实体
 */
data class Post(
    // 用户id
    val userId: Int,
    // id
    val id: Int,
    // 标题
    var title: String,
    // 文本内容
    val body: String,
    // 收藏标记
    val favorite : Boolean = false
)
