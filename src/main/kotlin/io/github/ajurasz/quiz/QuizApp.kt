package io.github.ajurasz.quiz

import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.*


fun main(args: Array<String>) {
    port(8080)

    before("/api", SecurityFilter())

    get("/") {_, _ -> "Quiz App"}
}

class SecurityFilter: Filter {
    override fun handle(request: Request, response: Response) {
        TODO("not implemented")
    }
}

