package org.autojs.autojs.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Test


class TopicServiceTest {

    @Test
    fun getScriptsTopics() {
        runBlocking{
        val topics = TopicService.getScriptsTopics()
        println(topics)
        }
    }
}