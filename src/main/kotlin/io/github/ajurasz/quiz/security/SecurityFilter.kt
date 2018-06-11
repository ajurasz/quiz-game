package io.github.ajurasz.quiz.security

import io.github.ajurasz.quiz.user.UserRepository
import org.eclipse.jetty.http.HttpStatus
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.halt

class SecurityFilter(private val userRepository: UserRepository): Filter {

    override fun handle(request: Request, response: Response) {
        request.headers(TOKEN_HEADER)?.let {
            if (!TokenUtils.verify(it)) {
                halt(HttpStatus.UNAUTHORIZED_401)
            }

            TokenUtils.get(it)?.let {
                if (!userRepository.exist(it.username, it.password)) {
                    halt(HttpStatus.UNAUTHORIZED_401)
                }
            } ?: run {
                halt(HttpStatus.UNAUTHORIZED_401)
            }
        } ?: run {
            halt(HttpStatus.UNAUTHORIZED_401)
        }
    }

    companion object {
        const val TOKEN_HEADER = "X-Token"
    }
}