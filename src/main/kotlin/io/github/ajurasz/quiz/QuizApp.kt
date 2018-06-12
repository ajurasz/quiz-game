package io.github.ajurasz.quiz

import com.google.inject.Guice
import com.google.inject.Inject
import io.github.ajurasz.quiz.security.SecurityFilter
import io.github.ajurasz.quiz.user.UserRepository
import spark.Spark.*


class QuizApp @Inject constructor(private val userRepository: UserRepository) {

    fun run(port: Int) {
        port(port)

        before("/api/*", SecurityFilter())

        get("/") {_, _ -> "Quiz App"}
        get("/api/secure") {_, _ -> "API"}
        get("/login") {_, _ ->
            userRepository.exist("admin", "admin")
        }
    }
}

fun main(args: Array<String>) {
    Guice.createInjector(QuizModule())
            .getInstance(QuizApp::class.java)
            .run(8080)
}
