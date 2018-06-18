package io.github.ajurasz.quiz.security

import io.github.ajurasz.quiz.user.User

object TokenUtils {

    enum class Claims {
        USERNAME
    }

    class CredentialsException : RuntimeException()

    @Throws(CredentialsException::class)
    fun validateAndGetClaims(token: String): Map<Claims, String> = emptyMap()

    fun generate(user: User): String = ""
}