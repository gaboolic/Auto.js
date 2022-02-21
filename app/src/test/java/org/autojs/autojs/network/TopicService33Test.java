package org.autojs.autojs.network;

import org.autojs.autojs.network.entity.topic.Topic;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TopicService33Test {

    @Test
    public void testGetScriptsTopics() throws IOException {
        List<Topic> list = TopicService33.getScriptsTopics();
        System.out.println(list);
    }
}