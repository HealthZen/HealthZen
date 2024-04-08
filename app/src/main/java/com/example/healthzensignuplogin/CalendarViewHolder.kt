package com.example.healthzensignuplogin

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthzensignuplogin.CalendarAdapter.OnItemListener
import java.time.LocalDate


class CalendarViewHolder(
    itemView: View,
    private val onItemListener: OnItemListener,
    days: ArrayList<LocalDate>
) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate>
    val parentView: View
    val dayOfMonth: TextView

    init {
        parentView = itemView.findViewById(R.id.parentView)
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        itemView.setOnClickListener(this)
        this.days = days
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}