package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthzensignuplogin.CalendarAdapter.OnItemListener
import com.example.healthzensignuplogin.CalendarUtils.daysInMonthArray
import com.example.healthzensignuplogin.CalendarUtils.monthYearFromDate
import java.time.LocalDate


class CalendarActivity : AppCompatActivity(), OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar2)
        initWidgets()
        CalendarUtils.selectedDate = LocalDate.now()
        setMonthView()

    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
    }

    private fun setMonthView() {
        monthYearText?.text = monthYearFromDate(CalendarUtils.selectedDate)
        val daysInMonth: ArrayList<LocalDate> = daysInMonthArray() ?: ArrayList()
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView?.layoutManager = layoutManager
        calendarRecyclerView?.adapter = calendarAdapter
    }

    fun previousMonthAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.plusMonths(1)
        setMonthView()
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        date?.let {
            CalendarUtils.selectedDate = it
            setMonthView()
        }
    }

    fun weeklyAction(view: View?) {
        startActivity(Intent(this, WeekViewActivity::class.java))
    }
}