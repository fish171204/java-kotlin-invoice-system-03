package com.example.baicongdiem03_002_nguyendangkhoa.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baicongdiem03_002_nguyendangkhoa.R
import com.example.baicongdiem03_002_nguyendangkhoa.Source.Purchase


class PurchaseAdapter(private val context: Context, private var purchases: List<Purchase>) :
    RecyclerView.Adapter<PurchaseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMaKH: TextView = view.findViewById(R.id.txtMaKH)
        val txtLoai: TextView = view.findViewById(R.id.rdbDVD )
        val txtSoLuong: TextView = view.findViewById(R.id.txtSoLuong)
        val txtDonGia: TextView = view.findViewById(R.id.spinnerDonGia)
        val txtThanhTien: TextView = view.findViewById(R.id.txtThanhTien)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val purchase = purchases[position]
        holder.txtMaKH.text = "Mã KH: ${purchase.maKH}"
        holder.txtLoai.text = "Loại: ${purchase.loai}"
        holder.txtSoLuong.text = "Số lượng: ${purchase.soLuong}"
        holder.txtDonGia.text = "Đơn giá: ${purchase.donGia} VNĐ"
        holder.txtThanhTien.text = "Thành tiền: ${purchase.thanhTien} VNĐ"
    }

    override fun getItemCount(): Int = purchases.size

    fun updateData(newPurchases: List<Purchase>) {
        purchases = newPurchases
        notifyDataSetChanged()
    }
}
