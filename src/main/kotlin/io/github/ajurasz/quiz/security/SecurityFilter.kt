package io.github.ajurasz.quiz.security

import org.eclipse.jetty.http.HttpStatus
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.halt

class SecurityFilter(private val tokenProvider: TokenProvider): Filter {

    override fun handle(request: Request, response: Response) {
        val token = request.headers(TOKEN_HEADER) ?: ""

        if (token.isEmpty()) {
            halt(HttpStatus.UNAUTHORIZED_401)
        }

        try {
            val username = tokenProvider.validateAndGetSubject(token)
            request.attribute(USER, username)
        } catch (e: TokenProvider.CredentialsException) {
            halt(HttpStatus.UNAUTHORIZED_401)
        }
    }

    companion object {
        const val TOKEN_HEADER = "X-Token"
        const val USER = "USER"
    }
}