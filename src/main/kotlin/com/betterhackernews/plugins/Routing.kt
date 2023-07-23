package com.betterhackernews.plugins

import com.betterhackernews.HackerNewsWrapper
import com.betterhackernews.respondCss
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.css.*
import kotlinx.html.*
import java.time.Instant

fun Application.configureRouting() {
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
                               // style = "width: 18rem;"
                                div("card-body") {
                                    div {
                                        a(href = story.url) {
                                            h4("card-title mb-1") {
                                                +"${idx + 1}: ${story.title!!}"
                                            }
                                        }
                                        div("blockquote-footer") {
                                            val user = story.by ?: "unknown"
                                            +"$user"
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

                                    div("card-footer text-body-secondary") {
                                        val timestampDif = (currentTimeStamp - story.time!!) / 3600
                                        +"$timestampDif hours ago"
                                    }

                                    story.topComments?.forEachIndexed { idx, comment ->
                                        div("m-2") {
                                            unsafe {
                                                val text = comment.text?.replace(Regex("<.*?>"), "") ?: ""
                                                +"${idx+1}: $text"
                                            }
                                            hr {  }
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

//        get("/get/{id}") {
//            call.parameters["id"]?.let {
//                call.respondText(hackerNewsWrapper.getItem(it.toInt()).toString())
//            }
//        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = rgb(246, 246, 239)
                }
//                rule("p.") {
//                    display = Display.inline
//                }
                rule("nav") {
                    backgroundColor = rgb(255,102,0)
                    color = Color.white
                }
                rule(".meta-data") {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                }
            }
        }

    }
}
