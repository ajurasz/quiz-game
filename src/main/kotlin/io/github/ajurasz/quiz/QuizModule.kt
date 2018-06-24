package io.github.ajurasz.quiz

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.google.inject.name.Names
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.ajurasz.quiz.security.SecurityFilter
import io.github.ajurasz.quiz.security.TokenProvider
import io.github.ajurasz.quiz.user.UserRepository
import io.github.ajurasz.quiz.user.UserRepositoryImpl
import org.jooq.Configuration
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultExecuteListener
import org.jooq.impl.DefaultExecuteListenerProvider
import java.util.*


class QuizModule: AbstractModule() {

    @Provides
    @Singleton
    fun configuration():  Configuration {

        val properties = Properties()
        properties.load(this::class.java.getResourceAsStream("/db/db.properties"))

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = properties.getProperty("db.url")
            username = properties.getProperty("db.username")
            password = properties.getProperty("db.password")
            maximumPoolSize = 20
            isAutoCommit = false
        }

        val dataSource = HikariDataSource(hikariConfig)

        return DefaultConfiguration()
                .set(DataSourceConnectionProvider(dataSource))
                .set(SQLDialect.POSTGRES)
                .set(DefaultExecuteListenerProvider(DefaultExecuteListener()))
    }

    @Provides
    @Singleton
    fun securityFilter(@Named("secret") secret: String) = SecurityFilter(TokenProvider(secret))

    override fun configure() {
        loadAppProperties()
        bind(UserRepository::class.java).to(UserRepositoryImpl::class.java).`in`(Singleton::class.java)
    }

    private fun loadAppProperties() {
        val properties = Properties()
        properties.load(this::class.java.getResourceAsStream("/app.properties"))
        Names.bindProperties(binder(), properties)
    }
}