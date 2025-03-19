package com.example.baicongdiem03_002_nguyendangkhoa

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class   HoaDonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hoadon_list)

        val gridViewInvoice: GridView = findViewById(R.id.gridViewInvoice)
        val btnClose: Button = findViewById(R.id.btnClose)

        val danhSachMuaHang = intent.getStringArrayListExtra("danhSachMuaHang") ?: arrayListOf()

        if (danhSachMuaHang.isEmpty()) {
            danhSachMuaHang.add("Không có hóa đơn nào.")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, danhSachMuaHang)
        gridViewInvoice.adapter = adapter

        btnClose.setOnClickListener {
            finish()
        }
    }
}
