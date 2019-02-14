package com.gudino.clima_ui.clima_ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gudino.clima_ui.clima_ui.R
import com.gudino.clima_ui.clima_ui.model.UIItem
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.TEXT_TYPE
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.TITLE_TYPE
import com.gudino.clima_ui.clima_ui.model.WeatherResponse
import kotlinx.android.synthetic.main.weather_detail_item_text.view.*
import kotlinx.android.synthetic.main.weather_detail_item_title.view.*
import java.util.*

class WeatherDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss"
    }

    private var items: ArrayList<UIItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_TYPE -> TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_detail_item_text, parent, false))
            TITLE_TYPE -> TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_detail_item_title, parent, false))
            else -> throw IllegalArgumentException("Cannot render other type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].type) {
            TEXT_TYPE -> (holder as TextViewHolder).bind(items[position].text)
            TITLE_TYPE -> (holder as TitleViewHolder).bind(items[position].text)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            0 -> TEXT_TYPE
            2 -> TITLE_TYPE
            else -> -1
        }
    }

    override fun getItemCount() = items.size

    fun loadDetail(weatherResponse: WeatherResponse) {
        items.clear()
        items.add(UIItem(TITLE_TYPE, weatherResponse.city.name.plus(" (${weatherResponse.city.country})")))
        weatherResponse.list.forEach {
            items.add(UIItem(TEXT_TYPE, it.dt_txt.plus(" Temperature: ${it.main.temp}")))
        }
        notifyDataSetChanged()
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String?) = with(itemView) {
            tv_text.text = text
        }
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(title: String?) = with(itemView) {
            tv_title.text = title
        }
    }
}