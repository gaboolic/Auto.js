package org.autojs.autojs.network;

import com.google.gson.Gson;

import org.autojs.autojs.network.entity.topic.DownloadScript;
import org.autojs.autojs.network.entity.topic.Topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopicService33 {

    public static List<Topic> getScriptsTopics() throws IOException {
        //创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //创建Request
        Request request = new Request.Builder()
                .url("https://zuocishenqi.chunqiujinjing.com//autojs/download")//访问连接
                .get()
                .build();
        //创建Call对象
        Call call = client.newCall(request);
        //通过execute()方法获得请求响应的Response对象
        Response response = call.execute();
        List<Topic> topics = new ArrayList<>();
        if (response.isSuccessful() && response.body() != null) {
            String json = response.body().string();
            Gson gson = new Gson();
            DownloadScript downloadScript = gson.fromJson(json, DownloadScript.class);
            for (String script: downloadScript.getData()){
                Topic topic = new Topic();
                topic.setTitle("script");
                topic.setTitleRaw(script);
                topics.add(topic);
            }
        }
        return topics;
    }
}
