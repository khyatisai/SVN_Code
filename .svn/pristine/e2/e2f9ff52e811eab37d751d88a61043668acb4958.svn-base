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
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.AppointmentReminderDAO
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.AppointmentReminder
import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_appointment_reminder_input.*
import kotlinx.android.synthetic.main.activity_appointment_reminder_input.txtTime
import kotlinx.android.synthetic.main.popup_period_date_input.*
import kotlinx.android.synthetic.main.popup_period_date_input.btnDone
import java.util.*

/**
 * Created by KhyatiShah on 10/21/2019.
 */
class AppointmentReminderInputActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    var selectedTime: String = ""
    var enteredDt: String = ""
    val CENTER_SELECTION_ACTIVITY_REQ = 100
    lateinit var centerId: String
    lateinit var centerName: String
    val permissionList = ArrayList<String>()
    val CONTACT_READ_PERMISSION_REQ_CODE = 100
    lateinit var appointmentReminderDAO: AppointmentReminderDAO

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_reminder_input)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.AppointmentReminder)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })
        enteredDt = DateFormat.format(Constant.PERIOD_DATE_FORMAT, Calendar.getInstance()).toString()
        btnAddReminder.setOnClickListener {
            if (TextUtils.isEmpty(edtAppointmentName.text)) {
                edtAppointmentName.error = getString(R.string.ErrorAppointmentName)
                edtAppointmentName.requestFocus()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(txtCenter.text)) {
                Toast.makeText(this, getString(R.string.SelectCenter), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(txtDate.text)) {
                Toast.makeText(this, getString(R.string.SelectDate), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(txtTime.text)) {
                Toast.makeText(this, getString(R.string.SelectTime), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //add appointment to DB
            appointmentReminderDAO =
                MhealthRoomDatabase.getAppDataBase(this!!)!!.appointmentReminderDAO()
            val appointmentReminder = AppointmentReminder(
                edtAppointmentName.text.toString(),
                centerId,
                centerName,
                selectedTime,
                enteredDt
            )
            appointmentReminderDAO.insert(
                appointmentReminder
            )
            //add notification for appointment
            //addNotificationForAppointment(appointmentReminderDAO.getLatestAppointment())
            AppUtils.setScheduledNotificationAppointment(appointmentReminderDAO.getLatestAppointment(),this)
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
                val appointmentReminder = appointmentReminderDAO.getLatestAppointment()
                //add event to calender
                AppUtils.addAppointmentReminder(appointmentReminder, this)
            }

            val intent = Intent()
            setResult(1, intent);
            finish()
        }

        txtDate.setOnClickListener {
            showDatePopup(getString(R.string.select_appointment_date))//Select Appointment Date
        }

        txtTime.setOnClickListener {
            showTImePopup()
        }
        txtCenter.setOnClickListener {
            val intent = Intent(this, CenterListActivity::class.java)
            startActivityForResult(intent, CENTER_SELECTION_ACTIVITY_REQ)
        }
    }

    fun addNotificationForAppointment(appointmentReminder: AppointmentReminder) {
        val notificationDAO =
            MhealthRoomDatabase.getAppDataBase(this!!.applicationContext)!!.notificationDAO()

        val dateTimeStr = AppUtils.dateConverter(
            Constant.PERIOD_DATE_FORMAT,
            Constant.APPOINTMENT_DISPLAY_DATE_FORMAT,
            appointmentReminder.date
        ) + " " + AppUtils.getTime12HourFormat(appointmentReminder.time)
        val notification = Notification(
            appointmentReminder.appointmentName,
            dateTimeStr,
            appointmentReminder.serviceCenterName,
            appointmentReminder.time,
            Constant.NOTIFICATION_TYPE_APPOINTMENT,
            "", false
        )
        notificationDAO.insert(notification)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CENTER_SELECTION_ACTIVITY_REQ) {
            if (resultCode == 1) {
                centerId = data!!.getStringExtra(Constant.CENTER_ID)
                centerName = data!!.getStringExtra(Constant.CENTER_NAME)
                txtCenter.text = centerName
            }
        }
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
//            R.id.home -> {
//                val intent = Intent()
//                setResult(2, intent);
//                finish()
//            }

            R.id.add -> {

            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun showTImePopup() {
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
                    val appointmentReminder = appointmentReminderDAO.getLatestAppointment()
                    //add event to calender
                    AppUtils.addAppointmentReminder(appointmentReminder, this)
                }
            }
        }
    }
}