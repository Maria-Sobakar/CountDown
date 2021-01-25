package com.marias.countdown.screens.newewent

import android.app.Activity
import android.os.Bundle
import com.google.android.material.timepicker.MaterialTimePicker
import android.os.Parcel
import android.text.format.DateFormat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.TimeFormat
import com.marias.countdown.R
import com.marias.countdown.databinding.NewEventBinding
import java.util.*

class NewEvent : AppCompatActivity() {

    private lateinit var binding: NewEventBinding
    private val viewModel: NewEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = NewEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.eventNameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.nameChanged(text)
        }
        binding.datePickerButton.setOnClickListener {
            val calendarConstraints = CalendarConstraints.Builder()
            calendarConstraints.setValidator(dateValidator())
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setCalendarConstraints(calendarConstraints.build())
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val targetDate = Calendar.getInstance()
                targetDate.time = Date(it)
                viewModel.dateIsSet(targetDate)
                binding.datePickerButton.text = DateFormat.format("dd.MM.yyyy", targetDate)
            }
            picker.show(supportFragmentManager, DIALOG_DATE)
        }
        binding.timePickerButton.setOnClickListener {
            val builder = MaterialTimePicker.Builder()
            builder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            builder.setTimeFormat(TimeFormat.CLOCK_24H)
            builder.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            builder.setMinute(Calendar.getInstance().get(Calendar.MINUTE))
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour
                val minute = picker.minute
                val targetTime = Calendar.getInstance()
                targetTime.set(0, 0, 0, hour, minute, 0)
                viewModel.timeIsSet(targetTime)
                binding.timePickerButton.text = DateFormat.format("kk:mm", targetTime)
            }
            picker.show(supportFragmentManager, DIALOG_TIME)
        }
        binding.addNewEventFAB.setOnClickListener {
            viewModel.addEvent(this)
        }

        viewModel.invalidTimeLiveData.observe(
            { this@NewEvent.lifecycle }) {
            it.getContentIfNotHandled()?.let {
               val snackbar =  Snackbar.make(
                    this,
                    binding.newEventCoordinatorLayout,
                    getText(R.string.invalid_time),
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
        }
    }

    private fun dateValidator() =
        object : CalendarConstraints.DateValidator {
            override fun describeContents(): Int {
                return 0
            }

            override fun writeToParcel(dest: Parcel?, flags: Int) {
            }

            override fun isValid(date: Long): Boolean {
                val minDate = Calendar.getInstance()
                minDate.set(Calendar.HOUR_OF_DAY, 0)
                minDate.set(Calendar.MINUTE, 0)
                minDate.set(Calendar.SECOND, 0)
                return date >= minDate.timeInMillis
            }
        }

    companion object {
        private const val DIALOG_DATE = "DialogDate"
        private const val DIALOG_TIME = "DialogTime"
    }
}