package com.bav.wbapp.promotions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bav.core.setupImage
import com.bav.wbapp.R
import java.util.Locale

class CarouselRVAdapter(private val data: List<String>) : RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class CarouselItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: String) {
            // FIXME() data: String to DataClass
            view.findViewById<ImageView>(R.id.promotionImage).setupImage(data)
            view.findViewById<TextView>(R.id.promotionTitle).apply {
                text = data.uppercase(Locale.getDefault())
            }
            view.findViewById<TextView>(R.id.promotionDate).apply {
                // TODO: text to date
            }
        }
    }
}