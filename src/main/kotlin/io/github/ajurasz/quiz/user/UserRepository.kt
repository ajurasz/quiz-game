package io.github.ajurasz.quiz.user

import com.google.inject.Inject
import io.github.ajurasz.quiz.db.tables.User.USER
import org.jooq.Configuration
import org.jooq.impl.DSL


interface UserRepository {
    fun findOne(username: String, password: String): User
}

class UserRepositoryImpl @Inject constructor(private val config: Configuration): UserRepository {

    override fun findOne(username: String, password: String): User {
        return DSL.using(config)
                .select()
                .from(USER)
                .where(USER.USERNAME.eq(username).and(
                        USER.PASSWORD.eq(password)
                ))
                .fetchAny()
                .into(User::class.java)
    }
}