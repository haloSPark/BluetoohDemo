package com.example.bluetoothdemo.data.repository

import android.content.Context
import android.util.Log
import com.example.bluetoothdatademo.data.DataStoreManager
import com.example.bluetoothdemo.data.api.RetrofitClient
import com.example.bluetoothdemo.data.api.RetrofitClient.apiService
import com.example.bluetoothdemo.data.model.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * post数据请求封装类
 */
class PostRepository (
    context: Context
) {
    private val dataStoreManager = DataStoreManager.getInstance(context)
    private var allPosts: List<Post>? = null

    /**
     * s获取帖子流，优先从网络获取，失败时使用本地缓存
     * @param page 页码
     * @param pageSize 每页数据个数
     */
    suspend fun getPosts(page: Int, pageSize: Int): List<Post>  {
        return withContext(Dispatchers.IO) {
            val all = getAllPosts()
            val pageItems = all.drop(page * pageSize).take(pageSize)
            pageItems.map { it.copy(favorite = dataStoreManager.isFavored(it.id)) }
        }
    }

    /**
     * 获取所有post
     * @return 所有post
     */
    private suspend fun getAllPosts(): List<Post> {
        if (allPosts != null) return allPosts!!
        return try {
            val remotePosts = apiService.getPosts()
            allPosts = remotePosts
            dataStoreManager.savePosts(remotePosts)
            remotePosts
        } catch (e: Exception) {
            val local = dataStoreManager.getCachedPosts()
            allPosts = local
            local
        }
    }

    /**
     * 收藏或取消收藏post
     * @param post post
     */
    fun toggleFavorite(post: Post) {
        dataStoreManager.toggleFavorite(post.id)
    }
}
