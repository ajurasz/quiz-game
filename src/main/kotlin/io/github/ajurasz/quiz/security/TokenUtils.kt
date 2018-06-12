package io.github.ajurasz.quiz.security

import io.github.ajurasz.quiz.user.User

object TokenUtils {
    fun verify(token: String): Boolean = true
    fun generate(user: User): String = ""
    fun username(token: String): String? = ""
}