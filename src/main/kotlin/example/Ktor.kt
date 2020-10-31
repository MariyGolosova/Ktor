package example

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.thymeleaf.Thymeleaf
import io.ktor.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

fun main() {
    val server = embeddedServer(Netty, port = 9090) {
        routing {

            install(Thymeleaf) {
                setTemplateResolver(
                        ClassLoaderTemplateResolver().apply {
                            prefix = "pages/"
                            suffix = ".html"
                            characterEncoding = "utf-8"
                        }
                )
            }

            get("/") {
                call.respondFile(File("./pages/sign.html"))
            }

            post("/sign") {

                val param = call.receiveParameters()
                val username = param["username"].toString()
                val password = param["password"].toString()

                //todo fix view information about user on page
                call.respond(ThymeleafContent("user",
                        mapOf("user" to User(username = username, password = password))))
            }
        }
    }
    server.start(wait = true)
}
