package com.example.bluetoothdemo.ui.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothdemo.data.repository.PostRepository
import com.example.bluetoothdemo.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * posts viewmodel
 */
class PostsViewModel(app: Application): AndroidViewModel(app) {

    private val repo = PostRepository(app)
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _noMoreData = MutableStateFlow(false)
    val noMoreData: StateFlow<Boolean> = _noMoreData

    // 当前page
    private var currentPage = 0
    // 每页加载数量
    private val pageSize = 5
    // 是否已全部加载
    private var allLoaded = false
    // 是否加载中
    private var isLoading = false

    init {
        refresh()
    }

    /**
     * 刷新
     */
    fun refresh() {
        currentPage = 0
        allLoaded = false
        _posts.value = emptyList()
        loadMore()
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        if (isLoading || allLoaded) return
        isLoading = true
        viewModelScope.launch {
            val newPosts = repo.getPosts(currentPage, pageSize)
            val updated = _posts.value.toMutableList().apply {
                addAll(newPosts)
            }
            _posts.value = updated
            if (newPosts.size < pageSize) {
                allLoaded = true
                _noMoreData.value = true
            }
            currentPage++
            isLoading = false

        }
    }

    /**
     * 收藏 or 取消收藏
     * @param post post
     */
    fun toggleFavorite(post: Post) {
        repo.toggleFavorite(post)
        val updated = _posts.value.map {
            if (it.id == post.id) it.copy(favorite = !it.favorite) else it
        }
        _posts.value = updated
    }
}
