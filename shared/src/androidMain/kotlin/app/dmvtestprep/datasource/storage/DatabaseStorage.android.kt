package app.dmvtestprep.datasource.storage

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.dmvtestprep.shared.db.AppDatabase

private var sqlDriverInstance: SqlDriver? = null

fun initializeSqlDriver(context: Context) {
    if (sqlDriverInstance == null) {
        sqlDriverInstance = AndroidSqliteDriver(AppDatabase.Schema, context, "app_database.db")
    }
}

actual fun provideSqlDriver(): SqlDriver {
    return sqlDriverInstance ?: throw IllegalStateException("SqlDriver must be initialized.")
}