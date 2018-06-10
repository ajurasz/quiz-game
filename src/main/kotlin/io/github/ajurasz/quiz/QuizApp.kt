package io.github.ajurasz.quiz

import com.google.inject.Guice
import com.google.inject.Inject
import io.github.ajurasz.quiz.user.UserRepository
import spark.Spark.get
import spark.Spark.port


class QuizApp @Inject constructor(private val userRepository: UserRepository) {

    fun run(port: Int) {
        port(port)
        get("/") {_, _ -> "Quiz App"}
        get("/login") {_, _ ->
            userRepository.findOne("admin", "admin")
        }
    }
}

fun main(args: Array<String>) {
    Guice.createInjector(QuizModule())
            .getInstance(QuizApp::class.java)
            .run(8080)
}
