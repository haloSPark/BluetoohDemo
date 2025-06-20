package com.example.bluetoothdatademo.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bluetoothdemo.data.model.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val DATASTORE_NAME = "app_prefs"
private val FAVORITE_POST_IDS = stringPreferencesKey("favorite_post_ids")
private val CACHED_POSTS_JSON = stringPreferencesKey("cached_posts_json")

/**
 * DataStore
 */
class DataStoreManager private constructor(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)
    private val gson = Gson()

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataStoreManager(context.applicationContext).also { INSTANCE = it }
            }
        }

    }

    /**
     * 收藏
     * @param postId ID
     */
    fun toggleFavorite(postId: Int) {
        runBlocking {
            context.dataStore.edit { prefs ->
                val current = prefs[FAVORITE_POST_IDS] ?: ""
                val set = current.split(",").filter { it.isNotBlank() }.toMutableSet()
                if (set.contains(postId.toString())) {
                    set.remove(postId.toString())
                } else {
                    set.add(postId.toString())
                }
                prefs[FAVORITE_POST_IDS] = set.joinToString(",")
            }
        }
    }

    fun isFavored(postId: Int): Boolean {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            val current = prefs[FAVORITE_POST_IDS] ?: ""
            current.split(",").contains(postId.toString())
        }
    }

    /**
     * 缓存
     * @param posts posts
     */
    suspend fun savePosts(posts: List<Post>) {
        val json = gson.toJson(posts)
        context.dataStore.edit { prefs ->
            prefs[CACHED_POSTS_JSON] = json
        }
    }

    /**
     * 读取缓存
     * @return posts
     */
    suspend fun getCachedPosts(): List<Post> {
        val prefs = context.dataStore.data.first()
        val json = prefs[CACHED_POSTS_JSON] ?: return emptyList()
        val type = object : TypeToken<List<Post>>() {}.type
        return gson.fromJson(json, type)
    }
}
