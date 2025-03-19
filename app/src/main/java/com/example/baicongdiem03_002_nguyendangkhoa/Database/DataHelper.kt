package com.example.baicongdiem03_002_nguyendangkhoa.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.baicongdiem03_002_nguyendangkhoa.Source.Purchase


class DataHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PurchaseDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Purchases"

        private const val COLUMN_ID = "id"
        private const val COLUMN_MAKH = "maKH"
        private const val COLUMN_LOAI = "loai"
        private const val COLUMN_SOLUONG = "soLuong"
        private const val COLUMN_DONGIA = "donGia"
        private const val COLUMN_THANHTIEN = "thanhTien"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MAKH TEXT,
                $COLUMN_LOAI TEXT,
                $COLUMN_SOLUONG INTEGER,
                $COLUMN_DONGIA INTEGER,
                $COLUMN_THANHTIEN INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addPurchase(maKH: String, loai: String, soLuong: Int, donGia: Int, thanhTien: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MAKH, maKH)
            put(COLUMN_LOAI, loai)
            put(COLUMN_SOLUONG, soLuong)
            put(COLUMN_DONGIA, donGia)
            put(COLUMN_THANHTIEN, thanhTien)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updatePurchase(id: Int, maKH: String, loai: String, soLuong: Int, donGia: Int, thanhTien: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MAKH, maKH)
            put(COLUMN_LOAI, loai)
            put(COLUMN_SOLUONG, soLuong)
            put(COLUMN_DONGIA, donGia)
            put(COLUMN_THANHTIEN, thanhTien)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun deletePurchase(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllPurchases(): List<Purchase> {
        val list = mutableListOf<Purchase>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        while (cursor.moveToNext()) {
            val purchase = Purchase(
                id = cursor.getInt(0),
                maKH = cursor.getString(1),
                loai = cursor.getString(2),
                soLuong = cursor.getInt(3),
                donGia = cursor.getInt(4),
                thanhTien = cursor.getInt(5)
            )
            list.add(purchase)
        }
        cursor.close()
        db.close()
        return list
    }
}
