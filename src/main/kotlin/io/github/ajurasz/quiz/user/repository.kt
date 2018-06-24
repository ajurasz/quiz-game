package io.github.ajurasz.quiz.user

import com.google.inject.Inject
import io.github.ajurasz.quiz.db.tables.User.USER
import io.github.ajurasz.quiz.hash
import org.jooq.Configuration
import org.jooq.impl.DSL


interface UserRepository {
    fun save(username: String, plainPassword: String): User
    fun exist(username: String): Boolean
    fun findOne(username: String): User
}

class UserRepositoryImpl @Inject constructor(private val config: Configuration) : UserRepository {

    override fun save(username: String, plainPassword: String): User {
        return DSL.using(config).transactionResult { ctx ->
            DSL.using(ctx)
                    .insertInto(USER, USER.USERNAME, USER.PASSWORD)
                    .values(username, plainPassword.hash())
                    .returning()
                    .fetchOne()
                    .into(User::class.java)
        }
    }

    override fun exist(username: String): Boolean {
        return DSL.using(config)
                .fetchExists(
                        DSL.select()
                                .from(USER)
                                .where(USER.USERNAME.eq(username))
                )
    }

    override fun findOne(username: String): User {
        return DSL.using(config)
                .select()
                .from(USER)
                .where(USER.USERNAME.eq(username))
                .fetchOptional()
                .map { it.into(User::class.java) }
                .orElseThrow { UserNotFoundException() }
    }
}