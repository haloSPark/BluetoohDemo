package com.example.bluetoothdemo.ui.posts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.example.bluetoothdemo.R
import com.example.bluetoothdemo.data.model.Post
import com.example.bluetoothdemo.databinding.FragmentPostsBinding
import com.example.bluetoothdemo.databinding.ItemPostBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 展示posts页面
 */
class PostsFragment : Fragment(R.layout.fragment_posts) {

    private val viewModel: PostsViewModel by viewModels()
    private lateinit var binding: FragmentPostsBinding

    override fun onViewCreated(v: View, s: Bundle?) {
        binding = FragmentPostsBinding.bind(v)
        binding.swipe.onRefresh {
            viewModel.refresh()
        }.autoRefresh()
        binding.swipe.onLoadMore {
            viewModel.loadMore()
        }

        binding.recycler.linear().setup {
            addType<Post>(R.layout.item_post)
            onClick(R.id.btnFav){
                viewModel.toggleFavorite(getModel())
            }
            onBind {
                val post = getModel<Post>()
                val itemPostBinding = getBinding<ItemPostBinding>()
                itemPostBinding.btnFav.setImageResource(
                    if (post.favorite) android.R.drawable.star_big_on else android.R.drawable.star_big_off
                )
            }
        }.models = viewModel.posts.value

        lifecycleScope.launch {
            viewModel.posts.collectLatest {
                binding.recycler.models = it
                binding.swipe.finishRefresh()
                binding.swipe.finishLoadMore()
            }
        }
        lifecycleScope.launch {
            viewModel.noMoreData.collectLatest {
                if (it) {
                    binding.swipe.finishLoadMoreWithNoMoreData()
                }
            }
            
        }
    }
}


