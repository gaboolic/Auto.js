package org.autojs.autojs.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.autojs.autojs.network.api.TopicApi
import org.autojs.autojs.network.api.UpdateCheckApi
import org.autojs.autojs.network.entity.topic.Topic
import org.autojs.autojs.network.util.WebkitCookieManagerProxy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TopicService {



    suspend fun getScriptsTopics(): List<Topic> {
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
            .client(
                OkHttpClient.Builder()
                    .build()
            ).build();
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