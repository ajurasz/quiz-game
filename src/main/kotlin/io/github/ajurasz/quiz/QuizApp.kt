package io.github.ajurasz.quiz

import com.google.inject.Guice
import com.google.inject.Inject
import io.github.ajurasz.quiz.security.SecurityFilter
import io.github.ajurasz.quiz.user.UserRepository
import spark.Spark.*


class QuizApp @Inject constructor(private val securityFilter: SecurityFilter,
                                  private val userRepository: UserRepository) {

    fun run(port: Int) {
        port(port)

        before("/api/*",securityFilter)

        get("/") {_, _ -> "Quiz App"}
        get("/api/secure") {_, _ -> "API"}
        get("/register") {_, _ ->
            val user = userRepository.save("admin1", "admin1")
            user
        }
        get("/login") {_, _ ->
            val user = userRepository.findOne("admin1")
            user.password.verifyHash("admin1")
        }
    }
}

fun main(args: Array<String>) {
    Guice.createInjector(QuizModule())
            .getInstance(QuizApp::class.java)
            .run(8080)
}
