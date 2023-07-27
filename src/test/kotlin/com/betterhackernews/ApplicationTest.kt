package com.betterhackernews

import com.betterhackernews.pages.HomePage
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.betterhackernews.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            HomePage()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
