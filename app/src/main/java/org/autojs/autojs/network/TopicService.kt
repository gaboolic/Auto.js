package org.autojs.autojs.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import org.autojs.autojs.network.api.TopicApi
import org.autojs.autojs.network.entity.topic.Post
import org.autojs.autojs.network.entity.topic.Topic
import org.autojs.autojs.network.util.WebkitCookieManagerProxy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TopicService {

    private const val CID_SCRIPTS = 9L
    private val mRetrofit = NodeBB.getInstance().retrofit
    private val mTopicApi = mRetrofit.create(TopicApi::class.java)

    suspend fun getTopics(cid: Long): List<Topic> {
        val category = mTopicApi.getCategory(cid).await()
        return category.topics.filter {
            it.appInfo != null
        }
    }

    suspend fun getMainPost(topic: Topic): Post {
        val fullTopic = mTopicApi.getTopic(topic.tid.toLong()).await()
        topic.mainPost = fullTopic.posts.first { post -> post.pid == topic.mainPid }
        return topic.mainPost
    }

    suspend fun getScriptsTopics(): List<Topic> {
        return getTopics(CID_SCRIPTS)
    }

    suspend fun getScriptsTopics2(): List<Topic> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://zuocishenqi.chunqiujinjing.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(
                OkHttpClient.Builder()
                    .cookieJar(WebkitCookieManagerProxy())
                    .build()
            )
            .build()
        val mTopicApi = retrofit.create(TopicApi::class.java);
        val downloadScript = mTopicApi.download().await()

        Log.d("market_fragment", downloadScript.toString())

        val mTopics = ArrayList<Topic>()

        for (script in downloadScript.data){
            val topic = Topic();
            topic.title = "script"
            topic.titleRaw = script;
            mTopics.add(topic);
        }

        return mTopics;
    }
}