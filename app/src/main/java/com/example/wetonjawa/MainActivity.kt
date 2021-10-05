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
import com.example.wetonjawa.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private val date: Calendar = Calendar.getInstance()
    private val year = date.get(Calendar.YEAR)
    private val month = date.get(Calendar.MONTH)
    private val day = date.get(Calendar.DAY_OF_MONTH)
    private val hour = date.get(Calendar.HOUR_OF_DAY)
    private val minute = date.get(Calendar.MINUTE)

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.showDate) {"$day ${monthName[month]} $year".also { text = it }}
        with(binding.showTime) {"${DecimalFormat("00").format(hour)} : ${DecimalFormat("00").format(minute)}"
            .also { text = it }}

        binding.selectDate.setOnClickListener(this)
        binding.selectTime.setOnClickListener(this)
        binding.generateWeton.setOnClickListener(this)

        if (savedInstanceState != null) {
            val result = savedInstanceState.getString(STATE_RESULT)
            binding.showWeton.text = result
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
        outState.putString(STATE_RESULT, binding.showWeton.text.toString())
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
                    "$dayOfMonth ${monthName[month]} $year".also { binding.showDate.text = it } },
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
                    .also { binding.showTime.text = it } },
                hour,
                minute,
                true
            )
            dialog.show()
        }

        if (v?.id == R.id.generate_weton) binding.showWeton.text = if (date.get(Calendar.YEAR) >= 1970) getWeton() else alert
    }
}