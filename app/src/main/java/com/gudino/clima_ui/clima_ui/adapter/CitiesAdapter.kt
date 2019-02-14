package com.gudino.clima_ui.clima_ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gudino.clima_ui.clima_ui.R
import com.gudino.clima_ui.clima_ui.model.CityResponse
import com.gudino.clima_ui.clima_ui.model.UIItem
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.HEADER_TYPE
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.PLUS_BUTTON_TYPE
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.TEXT_TYPE
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.TITLE_TYPE
import kotlinx.android.synthetic.main.weather_detail_item_plus_button.view.*
import kotlinx.android.synthetic.main.weather_detail_item_text.view.*
import kotlinx.android.synthetic.main.weather_detail_item_title.view.*

class CitiesAdapter(private val action: Action? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Action {
        fun tap()
        fun tapAndAdd(uiItem: UIItem)
    }

    private var items: ArrayList<UIItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UIItem.TEXT_TYPE -> TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_detail_item_text, parent, false))
            UIItem.PLUS_BUTTON_TYPE -> PlusButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_detail_item_plus_button, parent, false))
            UIItem.TITLE_TYPE -> TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_detail_item_title, parent, false))
            UIItem.HEADER_TYPE -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_detail_item_header, parent, false))
            else -> throw IllegalArgumentException("Cannot render other type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].type) {
            UIItem.TEXT_TYPE -> (holder as CitiesAdapter.TextViewHolder).bind(items[position].text)
            UIItem.PLUS_BUTTON_TYPE -> (holder as CitiesAdapter.PlusButtonViewHolder).bind(action!!)
            UIItem.TITLE_TYPE -> (holder as CitiesAdapter.TitleViewHolder).bind(items[position], action!!)
            UIItem.HEADER_TYPE -> holder as CitiesAdapter.HeaderViewHolder
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            0 -> UIItem.TEXT_TYPE
            1 -> UIItem.PLUS_BUTTON_TYPE
            2 -> UIItem.TITLE_TYPE
            3 -> UIItem.HEADER_TYPE
            else -> -1
        }
    }

    override fun getItemCount() = items.size

    fun addCity(uiItem: UIItem) {
        if (items.size > 0) {
            items.forEach {
                if (it.type == PLUS_BUTTON_TYPE) {
                    val position = items.indexOf(it)
                    items.add(position - 1, uiItem)
                    notifyDataSetChanged()
                }
            }
        } else {
            items.clear()
            items.add(UIItem(HEADER_TYPE))
            items.add(uiItem)
            items.add(UIItem(PLUS_BUTTON_TYPE))
            notifyDataSetChanged()
        }
    }

    fun addCityFromSearch(cityResponse: CityResponse?) {
        items.clear()
        if (cityResponse?.name != null) {
            items.add(UIItem(TITLE_TYPE, cityResponse.name.plus(", ${cityResponse.sys?.country}"), cityResponse.id))
        } else {
            items.add(UIItem(TEXT_TYPE, "City not found!"))
        }
        notifyDataSetChanged()
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String?) = with(itemView) {
            tv_text.text = text
        }
    }

    inner class PlusButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(action: Action) = with(itemView) {
            bt_add_city.setOnClickListener {
                action.tap()
            }
        }
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(uiItem: UIItem, action: Action) = with(itemView) {
            tv_title.text = uiItem.text
            setOnClickListener {
                action.tapAndAdd(uiItem)
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}