package com.example.baicongdiem03_002_nguyendangkhoa

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var txtMaKH: EditText
    private lateinit var txtSoLuong: EditText
    private lateinit var spinnerDonGia: Spinner
    private lateinit var txtThanhTien: EditText
    private lateinit var txtSoLuongCD: EditText
    private lateinit var txtSoLuongDVD: EditText
    private lateinit var txtTongTien: EditText
    private lateinit var ckbXuat: CheckBox
    private lateinit var cbxKhongXuat: CheckBox
    private lateinit var rdbCD: RadioButton
    private lateinit var rdbDVD: RadioButton
    private lateinit var btnThem: ImageButton
    private lateinit var btnCapNhat: ImageButton
    private lateinit var btnXoa: ImageButton
    private lateinit var btnThoat: ImageButton
    private lateinit var gridView: GridView

    private val danhSachMuaHang = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtMaKH = findViewById(R.id.txtMaKH)
        txtSoLuong = findViewById(R.id.txtSoLuong)
        spinnerDonGia = findViewById(R.id.spinnerDonGia)
        txtThanhTien = findViewById(R.id.txtThanhTien)
        txtSoLuongCD = findViewById(R.id.txtSoLuongCD)
        txtSoLuongDVD = findViewById(R.id.txtSoLuongDVD)
        txtTongTien = findViewById(R.id.txtTongTien)
        ckbXuat = findViewById(R.id.ckbXuat)
        cbxKhongXuat = findViewById(R.id.cbxKhongXuat)
        rdbCD = findViewById(R.id.rdbCD)
        rdbDVD = findViewById(R.id.rdbDVD)
        btnThem = findViewById(R.id.btnThem)
        btnCapNhat = findViewById(R.id.btnCapNhat)
        btnXoa = findViewById(R.id.btnXoa)
        btnThoat = findViewById(R.id.btnThoat)
        gridView = findViewById(R.id.gridView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, danhSachMuaHang)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
            val selectedItem = danhSachMuaHang[position]
            val parts = selectedItem.split(" | ")

            val maKH = parts[0].split(": ")[1]
            val loai = parts[1].split(": ")[1]
            val soLuong = parts[2].split(": ")[1].toInt()

            txtMaKH.setText(maKH)
            txtSoLuong.setText(soLuong.toString())

            if (loai == "CD") {
                txtSoLuongCD.setText((txtSoLuongCD.text.toString().toIntOrNull() ?: 0 + soLuong).toString())
            } else if (loai == "DVD") {
                txtSoLuongDVD.setText((txtSoLuongDVD.text.toString().toIntOrNull() ?: 0 + soLuong).toString())
            }

            tinhThanhTien()
        }


        rdbCD.setOnClickListener {
            rdbDVD.isChecked = false
        }

        rdbDVD.setOnClickListener {
            rdbCD.isChecked = false
        }

        cbxKhongXuat.setOnClickListener {
            if (cbxKhongXuat.isChecked) {
                ckbXuat.isChecked = false
            }
        }

        val donGiaArray = arrayOf("10000", "20000", "30000", "40000")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, donGiaArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDonGia.adapter = spinnerAdapter

        btnThem.setOnClickListener { themSanPham() }
        btnCapNhat.setOnClickListener { capNhatSanPham() }
        btnXoa.setOnClickListener { xoaSanPham() }
        btnThoat.setOnClickListener { thoatChuongTrinh() }

        txtSoLuong.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tinhThanhTien()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        spinnerDonGia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tinhThanhTien()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        ckbXuat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cbxKhongXuat.isChecked = false

                if (danhSachMuaHang.isEmpty()) {
                    Toast.makeText(this, "Không có hóa đơn để xuất!", Toast.LENGTH_SHORT).show()
                    ckbXuat.isChecked = false
                } else {
                    val intent = Intent(this, HoaDonActivity::class.java)
                    intent.putStringArrayListExtra("danhSachMuaHang", ArrayList(danhSachMuaHang))
                    startActivity(intent)
                }
            }
        }
    }

    private fun themSanPham() {
        val maKH = txtMaKH.text.toString()
        val soLuong = txtSoLuong.text.toString()
        val loai = if (rdbCD.isChecked) "CD" else "DVD"
        val donGia = spinnerDonGia.selectedItem.toString()
        val thanhTien = txtThanhTien.text.toString()

        if (maKH.isEmpty() || soLuong.isEmpty() || thanhTien.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            return
        }

        val item = "Mã KH: $maKH | Loại: $loai | Số lượng: $soLuong | Đơn giá: $donGia | Thành tiền: $thanhTien"
        danhSachMuaHang.add(item)
        adapter.notifyDataSetChanged()

        tinhTongTien()
        tinhTongSoLuongCDDVD()

        txtMaKH.text.clear()
        txtSoLuong.text.clear()
        txtThanhTien.text.clear()
        txtMaKH.requestFocus()
    }


    private fun capNhatSanPham() {
        if (selectedPosition == -1) {
            Toast.makeText(this, "Vui lòng chọn một mục để cập nhật!", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Xác nhận cập nhật")
            .setMessage("Bạn có muốn cập nhật thông tin?")
            .setPositiveButton("Có") { _, _ ->
                val updatedMaKH = txtMaKH.text.toString()
                val updatedSoLuong = txtSoLuong.text.toString()
                val updatedDonGia = spinnerDonGia.selectedItem.toString()
                val updatedThanhTien = txtThanhTien.text.toString()
                val updatedLoai = if (rdbCD.isChecked) "CD" else "DVD"

                val updatedItem = "Mã KH: $updatedMaKH | Loại: $updatedLoai | Số lượng: $updatedSoLuong | Đơn giá: $updatedDonGia | Thành tiền: $updatedThanhTien"
                danhSachMuaHang[selectedPosition] = updatedItem
                adapter.notifyDataSetChanged()

                tinhTongTien()
                tinhTongSoLuongCDDVD()
            }
            .setNegativeButton("Không", null)
            .show()
    }


    private fun xoaSanPham() {
        if (selectedPosition == -1) {
            Toast.makeText(this, "Vui lòng chọn một mục để xóa!", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có muốn xóa thông tin?")
            .setPositiveButton("Đồng ý") { _, _ ->
                danhSachMuaHang.removeAt(selectedPosition)
                adapter.notifyDataSetChanged()
                selectedPosition = -1

                tinhTongTien()
                tinhTongSoLuongCDDVD()
            }
            .setNegativeButton("Không", null)
            .show()
    }


    private fun thoatChuongTrinh() {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận thoát")
            .setMessage("Bạn có muốn thoát khỏi chương trình?")
            .setPositiveButton("Có") { _, _ -> finish() }
            .setNegativeButton("Không", null)
            .show()
    }

    private fun tinhThanhTien() {
        val soLuong = txtSoLuong.text.toString().toIntOrNull() ?: 0
        val donGia = spinnerDonGia.selectedItem.toString().toIntOrNull() ?: 0
        val thanhTien = soLuong * donGia
        txtThanhTien.setText(String.format("%,d VNĐ", thanhTien))
    }

    private fun tinhTongTien() {
        var tongTien = 0

        for (item in danhSachMuaHang) {
            val parts = item.split(" | ")
            val thanhTienStr = parts[4].split(": ")[1].replace(" VNĐ", "").replace(",", "")
            val thanhTien = thanhTienStr.toIntOrNull() ?: 0
            tongTien += thanhTien
        }

        txtTongTien.setText(String.format("%,d VNĐ", tongTien))
    }


    private fun tinhTongSoLuongCDDVD() {
        var tongCD = 0
        var tongDVD = 0

        for (item in danhSachMuaHang) {
            val parts = item.split(" | ")
            val loai = parts[1].split(": ")[1] // Lấy loại (CD hoặc DVD)
            val soLuong = parts[2].split(": ")[1].toIntOrNull() ?: 0 // Lấy số lượng

            if (loai == "CD") {
                tongCD += soLuong
            } else if (loai == "DVD") {
                tongDVD += soLuong
            }
        }
        txtSoLuongCD.setText(tongCD.toString())
        txtSoLuongDVD.setText(tongDVD.toString())
    }



}
