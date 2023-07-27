package com.betterhackernews

import com.betterhackernews.pages.HomePage
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.betterhackernews.plugins.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.css.CssBuilder

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

fun Application.module() {
    //configureSecurity()
    configureHTTP()
    //configureDatabases()
    HomePage()
}
