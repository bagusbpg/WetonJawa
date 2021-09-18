@file:Suppress("SpellCheckingInspection")

package com.example.wetonjawa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val date: Calendar = Calendar.getInstance()
    private val year = date.get(Calendar.YEAR)
    private val month = date.get(Calendar.MONTH)
    private val day = date.get(Calendar.DAY_OF_MONTH)
    private val hour = date.get(Calendar.HOUR_OF_DAY)
    private val minute = date.get(Calendar.MINUTE)

    private lateinit var selectDate: Button
    private lateinit var dateView: TextView
    private lateinit var selectTime: Button
    private lateinit var timeView: TextView
    private lateinit var generateWeton: Button
    private lateinit var wetonView: TextView

    private val monthName = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    private fun getWeton(): String {
        val dayName = listOf("Ngahad", "Senen", "Slasa", "Rebo", "Kemis", "Jemuwah", "Setu")
        val pasaran = listOf("Kliwon", "Legi", "Pahing", "Pon", "Wage")

        var dayIndex = date.get(Calendar.DAY_OF_WEEK) % 7
        var pasaranIndex = date.timeInMillis/86400000 % 5

        if (date.get(Calendar.HOUR_OF_DAY) < 18) {
            dayIndex = (date.get(Calendar.DAY_OF_WEEK) - 1) % 7
            pasaranIndex = (date.timeInMillis/86400000 - 1) % 5
        }

        return "${dayName[dayIndex]} ${pasaran[pasaranIndex.toInt()]}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectDate = findViewById(R.id.select_date)
        dateView = findViewById(R.id.show_date)
        selectTime = findViewById(R.id.select_time)
        timeView = findViewById(R.id.show_time)
        generateWeton = findViewById(R.id.generate_weton)
        wetonView = findViewById(R.id.show_weton)

        with(dateView) {"$day ${monthName[month]} $year".also { text = it }}
        with(timeView) {"${DecimalFormat("00").format(hour)} : ${DecimalFormat("00").format(minute)}"
            .also { text = it }}

        selectDate.setOnClickListener(this)
        selectTime.setOnClickListener(this)
        generateWeton.setOnClickListener(this)

        if (savedInstanceState != null) {
            val result = savedInstanceState.getString(STATE_RESULT)
            wetonView.text = result
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val about = Intent(this@MainActivity, AboutMe::class.java)
        startActivity(about)
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, wetonView.text.toString())
    }

    override fun onClick(v: View?) {
        val alert = "The year must be 1970 or above."

        if (v?.id == R.id.select_date) {
            val dialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    date.set(Calendar.YEAR, year)
                    date.set(Calendar.MONTH, month)
                    date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    "$dayOfMonth ${monthName[month]} $year".also { dateView.text = it } },
                year,
                month,
                day
            )
            dialog.show()
        }

        if (v?.id == R.id.select_time) {
            val dialog = TimePickerDialog(
                this,
                { _, hour, minute ->
                    date.set(Calendar.HOUR_OF_DAY, hour)
                    date.set(Calendar.MINUTE, minute)
                    "${DecimalFormat("00").format(hour)} : ${DecimalFormat("00").format(minute)}"
                    .also { timeView.text = it } },
                hour,
                minute,
                true
            )
            dialog.show()
        }

        if (v?.id == R.id.generate_weton) wetonView.text = if (date.get(Calendar.YEAR) >= 1970) getWeton() else alert
    }
}