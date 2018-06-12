package io.github.ajurasz.quiz.security

import org.eclipse.jetty.http.HttpStatus
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.halt

class SecurityFilter: Filter {

    override fun handle(request: Request, response: Response) {
        val token = request.headers(TOKEN_HEADER) ?: ""

        if (token.isEmpty()) {
            halt(HttpStatus.UNAUTHORIZED_401)
        }

        if (!TokenUtils.verify(token)) {
            halt(HttpStatus.UNAUTHORIZED_401)
        }

        val username = TokenUtils.username(token)

        when (username) {
            null -> halt(HttpStatus.UNAUTHORIZED_401)
            else ->  request.attribute(USER, username)
        }
    }

    companion object {
        const val TOKEN_HEADER = "X-Token"
        const val USER = "USER"
    }
}