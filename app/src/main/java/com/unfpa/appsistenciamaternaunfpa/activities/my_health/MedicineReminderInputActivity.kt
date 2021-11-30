package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.Manifest
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.InputFilter
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.MedicineReminderDAO
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.utils.InputFilterMinMax
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_medicine_reminder_input.*
import kotlinx.android.synthetic.main.activity_medicine_reminder_input.btnDone
import kotlinx.android.synthetic.main.activity_medicine_reminder_input.txtDate
import kotlinx.android.synthetic.main.activity_medicine_reminder_input.txtTime
import kotlinx.android.synthetic.main.popup_period_date_input.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by KhyatiShah on 10/16/2019.
 */
class MedicineReminderInputActivity : AppCompatActivity(), View.OnClickListener {


    private var toolbar: Toolbar? = null
    var selectedTime: String = ""
    var reminderBefore: String = ""
    val CONTACT_READ_PERMISSION_REQ_CODE = 100
    lateinit var medicineReminder: MedicineReminder
    val permissionList = ArrayList<String>()
    lateinit var medicineReminderDAO: MedicineReminderDAO
    var enteredDt: String = ""

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_reminder_input)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.MedicineReminder)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })
        enteredDt = DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        txt15Min.setOnClickListener(this)
        txt30Min.setOnClickListener(this)
        txt1Hour.setOnClickListener(this)
        txt2Hour.setOnClickListener(this)

        edtDose.setFilters(arrayOf<InputFilter>(InputFilterMinMax(1, 100)))
        edtDays.setFilters(arrayOf<InputFilter>(InputFilterMinMax(1, 365)))

        txtDate.setOnClickListener {
            showDatePopup(getString(R.string.select_appointment_date))//Select Appointment Date
        }
        txtTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                try {
                    //Toast.makeText(this, h.toString() + " : " + m + " : ", Toast.LENGTH_LONG).show()
                    selectedTime = h.toString() + ":" + m
                    txtTime.text = AppUtils.getTime12HourFormat(selectedTime)
                    txtTime.error = null

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }), hour, minute, false)

            tpd.show()
        }

        btnSaveAdd.setOnClickListener {
            medicineReminderDAO =
                MhealthRoomDatabase.getAppDataBase(this!!.applicationContext)!!.medicineReminderDAO()

            if (TextUtils.isEmpty(txtDate.text)) {
                Toast.makeText(this, getString(R.string.SelectDate), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(txtTime.text)) {
                Toast.makeText(this, getString(R.string.SelectTime), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(edtMedicineName.text)) {
                edtMedicineName.error = getString(R.string.EnterMedicineName)
                edtMedicineName.requestFocus()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(edtDose.text)) {
                edtDose.error = getString(R.string.EnterDose)
                edtDose.requestFocus()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(edtDays.text)) {
                edtDays.error = getString(R.string.EnterDays)
                edtDays.requestFocus()
                return@setOnClickListener
            }
            medicineReminder = MedicineReminder(
                enteredDt,
                selectedTime,
                radioWithFood.isChecked,
                edtMedicineName.text.toString(),
                edtDose.text.toString().toInt(),
                edtDays.text.toString().toInt(),
                reminderBefore
            )
            medicineReminderDAO.insert(
                medicineReminder
            )
            //insert data for notification
            AppUtils.setScheduledNotificationMedicine(medicineReminderDAO.getLatestReminder(), this)
            clearData()

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(Manifest.permission.READ_CALENDAR)
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(Manifest.permission.WRITE_CALENDAR)
            }
            if (permissionList.size > 0) {
                val array = arrayOfNulls<String>(permissionList.size)

                ActivityCompat.requestPermissions(
                    this,
                    permissionList.toArray(array),
                    CONTACT_READ_PERMISSION_REQ_CODE
                )
            } else {
                AppUtils.addCalenderReminderMedicine(medicineReminderDAO.getLatestReminder(), this)
            }


        }

        btnDone.setOnClickListener {
            val intent = Intent()
            setResult(1, intent);
            finish()
        }
    }

    fun showDatePopup(message: String) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_period_date_input)
        dialog.btnDone.setOnClickListener {
            txtDate.text = enteredDt
            dialog.dismiss()
        }
        dialog.txtHeader.text = message
        dialog.calendarView?.minDate = Calendar.getInstance().timeInMillis
        dialog.calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            enteredDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            // txtDate.text = enteredDt
        }
        dialog.txtClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            CONTACT_READ_PERMISSION_REQ_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {
                    AppUtils.addCalenderReminderMedicine(medicineReminderDAO.getLatestReminder(), this)
                }
            }
        }
    }

    fun clearData() {
        selectedTime = ""
        txtTime.text = ""
        edtMedicineName.text.clear()
        edtDose.text.clear()
        edtDays.text.clear()
        txt15Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
        txt30Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
        txt1Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
        txt2Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
        txt15Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
        txt30Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
        txt1Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
        txt2Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
        radioWithFood.isChecked = false
        reminderBefore = ""

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        if (menu != null) {
            menu.findItem(R.id.home).isVisible = true
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.add).isVisible = false
            menu.findItem(R.id.calendar).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent()
                setResult(2, intent);
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txt15Min -> {
                txt15Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_blue_edittext)
                txt30Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt1Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt2Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt15Min.setTextColor(ContextCompat.getColor(this, R.color.white))
                txt30Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt1Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt2Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                reminderBefore = txt15Min.text.toString()
            }

            R.id.txt30Min -> {
                txt30Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_blue_edittext)
                txt15Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt1Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt2Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt30Min.setTextColor(ContextCompat.getColor(this, R.color.white))
                txt15Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt1Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt2Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                reminderBefore = txt30Min.text.toString()
            }

            R.id.txt1Hour -> {
                txt1Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_blue_edittext)
                txt15Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt30Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt2Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt1Hour.setTextColor(ContextCompat.getColor(this, R.color.white))
                txt15Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt30Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt2Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                reminderBefore = txt1Hour.text.toString()
            }
            R.id.txt2Hour -> {
                txt2Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_blue_edittext)
                txt15Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt30Min.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt1Hour.background = ContextCompat.getDrawable(this, R.drawable.rounded_border_edittext)
                txt2Hour.setTextColor(ContextCompat.getColor(this, R.color.white))
                txt15Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt30Min.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                txt1Hour.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                reminderBefore = txt2Hour.text.toString()
            }
        }
    }


}
