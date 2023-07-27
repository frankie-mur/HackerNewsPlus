package com.betterhackernews.pages

import com.betterhackernews.HackerNewsWrapper
import com.betterhackernews.respondCss
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.css.*
import kotlinx.html.*
import java.net.URL
import java.time.Instant

fun Application.HomePage() {
    val hackerNewsWrapper = HackerNewsWrapper()
    routing {
        get("/") {
            val bestStories = hackerNewsWrapper.topStories()
            //TODO: Add spinner
            call.respondHtml(HttpStatusCode.OK) {
                    head {
                        title {
                            +"Hacker News+"
                        }
                        meta {
                            name = "viewport"
                            content = "width=device-width"
                            charset = "utf-8"
                        }
                        link {
                            href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
                            rel="stylesheet"
                        }
                        link {
                            rel = "stylesheet"
                            href = "/styles.css"
                            type = "text/css"
                        }
                    }

                    body {
                        nav("navbar bg-body-tertiary") {
                            div("container-fluid") {
                                a(classes="navbar-brand") {
                                    +"Hacker News+"
                                }
                            }
                        }
                        val currentTimeStamp = Instant.now().epochSecond
                        bestStories.forEachIndexed { idx, story ->
                            div("card m-5") {
                                div("card-body") {
                                    div {
                                        div {
                                            a(href = story.url) {
                                                val extractedLink =
                                                    if (!story.url.isNullOrEmpty()) URL(story.url).host.removePrefix("www.") else ""
                                                h4("d-inline-block card-title mb-1") {
                                                    +"${idx + 1}: ${story.title!!}"
                                                }
                                                h6(classes = "d-inline-block title-url") {
                                                    +"($extractedLink)"
                                                }
                                            }
                                        }
                                        div("blockquote-footer") {
                                            val user = story.by ?: "unknown"
                                            +user
                                        }
                                        div("meta-data mt-2") {
                                            p {
                                                +"${story.score!!}"
                                            }
                                            i(classes = "ml-1 pt-1 fa-solid fa-caret-up") {}
                                            p("ml-3") {
                                                val numComments = story.descendants ?: 0
                                                +"$numComments"
                                            }
                                            i(classes = "ml-1 pt-1 fa-regular fa-comment-dots") {}
                                        }
                                    }

                                    div("card-footer text-body-secondary mb-2") {
                                        val timestampDif = (currentTimeStamp - story.time!!) / 3600
                                        +"$timestampDif hours ago"
                                    }
//                                    if (!story.text.isNullOrBlank()){
//                                        p {
//                                            +story.text
//                                        }
//                                    }
                                    div(classes="text-center") {
                                        h6 { +"Top Comments "}
                                    }
                                    story.topComments?.forEachIndexed { idx, comment ->
                                        div("m-2 comments") {
                                            unsafe {
                                                val text = comment.text?.replace(Regex("<.*?>"), "") ?: ""
                                                +"${idx+1}: $text"
                                            }
                                            hr {  }
                                        }
                                    }
                                    val url = "https://news.ycombinator.com/item?id=${story.id}"
                                    div(classes="d-flex align-items-center justify-content-center") {
                                        a(href = url) {
                                            i(classes = "fa-brands fa-hacker-news") {}
                                        }
                                    }
                                }
                            }
                        }

                        script {
                            src = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
                        }
                        script {
                            src = "https://kit.fontawesome.com/d4e24e9720.js"
                        }
                    }
                }
            }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = rgb(246, 246, 239)
                }
                rule("nav") {
                    backgroundColor = rgb(255,102,0)
                    color = Color.white
                }
                rule(".meta-data") {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                }
                rule("a") {
                    color = Color.inherit
                }
                rule(".title-url") {
                    color = rgb(139,139,139)
                }
                rule(".comments") {
                    fontSize = LinearDimension.auto
                }
            }
        }

    }
}
