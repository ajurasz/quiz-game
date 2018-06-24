package io.github.ajurasz.quiz.security

import io.github.ajurasz.quiz.user.User
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


class TokenProvider(secret: String) {
    private val signatureAlgorithm = SignatureAlgorithm.HS512
    private val key: SecretKey

    init {
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret)
        key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
    }

    class CredentialsException : RuntimeException()

    @Throws(CredentialsException::class)
    fun validateAndGetSubject(token: String): String {
        try {
            val claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJwt(token)
                    .body
            return claims.subject
        } catch (e: JwtException) {
            throw CredentialsException()
        }
    }

    fun generate(user: User): String = Jwts.builder()
            .setIssuer("quiz.com")
            .setSubject(user.username)
            .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .setExpiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
            .signWith(signatureAlgorithm, key)
            .compact()
}