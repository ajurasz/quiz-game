package io.github.ajurasz.quiz.user

import com.google.gson.Gson
import spark.Request

val gson = Gson()

// Spark
inline fun <reified T> Request.json() = gson.fromJson(this.body(), T::class.java)