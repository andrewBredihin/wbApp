package com.bav.wbapp.promotions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bav.core.promotions.Promotion
import com.bav.core.setupImage
import com.bav.wbapp.R
import java.util.Locale

class CarouselRVAdapter(private val data: List<Promotion>) : RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class CarouselItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Promotion) {
            // TODO: image from data
            val url = "https://i.pinimg.com/236x/0e/bd/26/0ebd262c4b7f69f7ec915dbd8509328f.jpg"
            view.findViewById<ImageView>(R.id.promotionImage).setupImage(url)
            view.findViewById<TextView>(R.id.promotionTitle).apply {
                text = data.title.uppercase(Locale.getDefault())
            }
            view.findViewById<TextView>(R.id.promotionDate).apply {
                text = data.date
            }
        }
    }
}