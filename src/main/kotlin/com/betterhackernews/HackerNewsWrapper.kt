package com.betterhackernews

import com.betterhackernews.domain.Item
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

data class HackerNewsWrapper(val url: String? = null) {
    private val httpClient = HttpClient(CIO) {
        defaultRequest {
            url("https://hacker-news.firebaseio.com/")
        }
        install(Logging)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
        expectSuccess = true
    }

    private suspend fun getItem(id: Int): Item {
        val item = httpClient.get("/v0/item/$id.json").body<Item>()
        return when (item.type) {
            "story" -> {
                val topNComments = item.addTopNComments(3)
                item.topComments = topNComments
                item
            }
            else ->  item
        }
    }

    suspend fun topStories(): List<Item> =
        coroutineScope {
            httpClient.get("/v0/topstories.json").let {
                val listOfStoryIds = it.body<List<Int>>().take(25)
                listOfStoryIds.map {
                    async { getItem(it) }
                }.awaitAll()
            }
        }

    private suspend fun Item.addTopNComments(n: Int): List<Item> =
        coroutineScope {
            //can we not use take as it creates a new list everytime
            this@addTopNComments.kids.take(n).map { comment: Int ->
                async { getItem(comment) }
            }.awaitAll()
        }

}