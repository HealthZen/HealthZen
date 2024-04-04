package com.example.healthzensignuplogin

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthzensignuplogin.CalendarUtils.formattedDate
import com.example.healthzensignuplogin.CalendarUtils.formattedTime
import java.time.LocalTime


class EventEditActivity : AppCompatActivity() {
    private var eventNameET: EditText? = null
    private var eventDateTV: TextView? = null
    private var eventTimeTV: TextView? = null
    private var time: LocalTime = LocalTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)
        initWidgets()
        time = LocalTime.now()
        eventDateTV!!.text = "Date: " + formattedDate(
            CalendarUtils.selectedDate!!
        )
        eventTimeTV!!.text = "Time: " + formattedTime(time)
    }

    private fun initWidgets() {
        eventNameET = findViewById(R.id.eventNameET)
        eventDateTV = findViewById(R.id.eventDateTV)
        eventTimeTV = findViewById(R.id.eventTimeTV)
    }

    fun saveEventAction(view: View?) {
        val eventName = eventNameET!!.text.toString()
        val newEvent = Event(
            eventName,
            CalendarUtils.selectedDate!!, time!!
        )
        Event.eventsList.add(newEvent)
        finish()
    }
}