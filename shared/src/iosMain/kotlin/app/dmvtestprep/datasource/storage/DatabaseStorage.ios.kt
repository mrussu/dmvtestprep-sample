package app.dmvtestprep.datasource.storage

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.dmvtestprep.shared.db.AppDatabase

actual fun provideSqlDriver(): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Schema, "app_database.db")
}