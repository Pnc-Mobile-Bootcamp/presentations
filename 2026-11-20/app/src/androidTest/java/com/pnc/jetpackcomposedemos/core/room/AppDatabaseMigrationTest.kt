package com.pnc.jetpackcomposedemos.core.room

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class AppDatabaseMigrationTest {

    private val testDbName = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    // ----------- test migrating from version 1 to version 2
    @Test
    fun migrate1To2_keepsExistingToDosAndAddsTheOrderTables() {

        helper.createDatabase(testDbName, 1).apply {
            execSQL("INSERT INTO todo (id, title, completed) VALUES (1, 'Buy milk', 0)")
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 2, true)

        // make sure the to do item is still there
        db.query("SELECT title, completed FROM todo").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Buy milk", cursor.getString(0))
            assertEquals(0, cursor.getInt(1))
        }
        // make sure the orders table is there and there are no records in it
        db.query("SELECT COUNT(*) FROM orders").use { cursor ->
            cursor.moveToFirst()
            assertEquals(0, cursor.getInt(0))
        }
        db.query("SELECT COUNT(*) FROM order_line_items").use { cursor ->
            cursor.moveToFirst()
            assertEquals(0, cursor.getInt(0))
        }

    }

    @Test
    fun migrate2To3_keepsOrdersAndAddsTheArtistsTable() {

        helper.createDatabase(testDbName, 2).apply {
            execSQL("INSERT INTO orders (id, customerId, firstName, lastName, orderDate, shipDate) " +
            "VALUES (1, 100, 'Ada', 'Lovelace', 1704067200000, 1704240000000)"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDbName, 3, true)

        db.query("SELECT firstName FROM orders WHERE id = 1").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Ada", cursor.getString(0))
        }
        db.query("SELECT COUNT(*) FROM artists").use { cursor ->
            cursor.moveToFirst()
            assertEquals(0, cursor.getInt(0))
        }

    }




}




