package io.github.ajurasz.quiz

import com.google.gson.Gson
import org.mindrot.jbcrypt.BCrypt
import spark.Request

val gson = Gson()
val bCryptSalt = BCrypt.gensalt(12)

// Spark
inline fun <reified T> Request.json() = gson.fromJson(this.body(), T::class.java)

// BCrypt
fun String.hash() = BCrypt.hashpw(this, bCryptSalt)!!
fun String.verifyHash(plain: String) = BCrypt.checkpw(plain, this)