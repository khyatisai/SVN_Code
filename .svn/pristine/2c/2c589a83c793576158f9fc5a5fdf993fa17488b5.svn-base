package com.unfpa.appsistenciamaternaunfpa.fragments.my_health

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.AppointmentReminderInputActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.MedicineReminderInputActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PeriodListActivity
import com.unfpa.appsistenciamaternaunfpa.activities.my_health.PeriodTrackerInputActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.my_health.AppointmentReminderAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_health.MedicineReminderAdapter
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.PeriodTrackerDAO
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.PeriodTracker
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.Listener
import kotlinx.android.synthetic.main.dialog_delete_reminder.*
import kotlinx.android.synthetic.main.fragment_my_health_landing.*
import kotlinx.android.synthetic.main.popup_period_date_input.*
import kotlinx.android.synthetic.main.popup_period_date_input.btnDone
import kotlinx.android.synthetic.main.popup_period_date_input.calendarView
import kotlinx.android.synthetic.main.popup_period_date_input.txtHeader
import java.text.SimpleDateFormat
import java.util.*

class MyHealthLandingFragment : androidx.fragment.app.Fragment(), Listener {


    private var isPeriodTrackerEmpty: Boolean = false
    private var isMedicationReminderEmpty: Boolean = true
    private var isAppointsReminderEmpty: Boolean = true
    lateinit var periodTrackerDAO: PeriodTrackerDAO
    var selectedDt: String = ""
    var periodLength = 0
    lateinit var periodList: List<PeriodTracker>
    lateinit var medicineReminderList: List<MedicineReminder>
    private val REQ_CODE_PERIOD_TRACKER = 100
    private val REQ_CODE_MEDICINE_REMINDER = 200
    private val REQ_CODE_APPOINTMENT_REMINDER = 300
    private var forgotAddPeriod: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_my_health_landing, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setting actionbar title
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.menu_my_health)
        imgAddPeriod.setOnClickListener {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.MENSTRUAL_TRACKER, activity as AppCompatActivity)

            val intent = Intent(activity, PeriodTrackerInputActivity::class.java)
            startActivityForResult(intent, REQ_CODE_PERIOD_TRACKER)

        }
        cardMedicationReminderEmpty.setOnClickListener {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.OTHER_HEALTH_TOOL, activity as AppCompatActivity)

            val intent = Intent(activity, MedicineReminderInputActivity::class.java)
            startActivityForResult(intent, REQ_CODE_MEDICINE_REMINDER)
        }

        btnLogPeriod.setOnClickListener {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.MENSTRUAL_TRACKER, activity as AppCompatActivity)

            if (btnLogPeriod.text.toString().equals((activity as AppCompatActivity).getString(R.string.EndPeriod))) {
                showEndDatePopup((activity as AppCompatActivity).getString(R.string.EnterEndDate))
            } else {
                showStartDatePopup((activity as AppCompatActivity).getString(R.string.EnterStartDate))
            }
        }
        //Add appointment
        cardAppointsReminderEmpty.setOnClickListener {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.OTHER_HEALTH_TOOL, activity as AppCompatActivity)

            val intent = Intent(activity, AppointmentReminderInputActivity::class.java)
            startActivityForResult(intent, REQ_CODE_APPOINTMENT_REMINDER)
        }
        //see all period list
        llShowPeriod.setOnClickListener {
            val intent = Intent(activity, PeriodListActivity::class.java)
            startActivity(intent)
        }
        setupPeriodTrackingUI()
        setMedicineReminderUI()
        setupAppointmentUI()
    }

    //Logic for first time input for period tracker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2) {
            (activity as MainActivity).setMenuChecked(0)
        }
        if (requestCode == REQ_CODE_PERIOD_TRACKER) {
            if (resultCode == 1) {
                if (data != null) {
                    selectedDt = data.getStringExtra(Constant.SELECTED_DATE)
                    periodLength = data.getIntExtra(Constant.NO_OF_DAYS, 0)
                    if (!TextUtils.isEmpty(selectedDt) && periodLength != 0)
                        periodTrackerDAO.insert(PeriodTracker(selectedDt, "", periodLength, 28))
                    setupPeriodTrackingUI()
                }
            }
        }
        if (requestCode == REQ_CODE_MEDICINE_REMINDER) {
            setMedicineReminderUI()
        }
        if (requestCode == REQ_CODE_APPOINTMENT_REMINDER) {
            setupAppointmentUI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.home).isVisible = true
        }
    }

    fun setMedicineReminderUI() {
        val medicineReminderDAO =
            MhealthRoomDatabase.getAppDataBase(this!!.activity!!)!!.medicineReminderDAO()
        medicineReminderList = medicineReminderDAO.getAllMedicine()
        isMedicationReminderEmpty = medicineReminderList.isEmpty()
        lstMedicineReminder.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.HORIZONTAL, false)
        val adapter = MedicineReminderAdapter(activity!!, this)
        lstMedicineReminder.adapter = adapter
        adapter.setMedicineReminderList(medicineReminderList)

        if (isMedicationReminderEmpty) {
            cardMedicationReminderEmpty.visibility = View.VISIBLE
            txtNoReminder.visibility = View.VISIBLE
            lstMedicineReminder.visibility = View.GONE
        } else {
            cardMedicationReminderEmpty.visibility = View.GONE
            txtNoReminder.visibility = View.GONE
            lstMedicineReminder.visibility = View.VISIBLE
        }
    }

    fun setupPeriodTrackingUI() {
        periodTrackerDAO = MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.periodTrackerDAO()
        periodList = periodTrackerDAO.getAllPeriods()
        isPeriodTrackerEmpty = periodList.isEmpty()

        if (isPeriodTrackerEmpty) {
            cardPeriodTrackerEmpty.visibility = View.VISIBLE
            cardPeriodTrackerFilled.visibility = View.GONE
        } else {
            cardPeriodTrackerEmpty.visibility = View.GONE
            cardPeriodTrackerFilled.visibility = View.VISIBLE
            //setting period detials
            setPeriodDetails()
        }


    }

    fun setupAppointmentUI() {
        val appointmentReminderDAO =
            MhealthRoomDatabase.getAppDataBase(activity!!)!!.appointmentReminderDAO()
        val listAppointments = appointmentReminderDAO.getAllAppointments()
        isAppointsReminderEmpty = listAppointments.isEmpty()
        lstAppointmentReminder.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)
        val delListener = object : Listener {
            override fun onComplete(response: String) {
                if (response.equals("add")) {
                    val intent = Intent(activity, AppointmentReminderInputActivity::class.java)
                    startActivityForResult(intent, REQ_CODE_APPOINTMENT_REMINDER)
                } else {//remove reminder from medicine reminder list
                    val appointmentReminderDAO =
                        MhealthRoomDatabase.getAppDataBase(activity!!)!!.appointmentReminderDAO()
                    val strEventURI = appointmentReminderDAO.getEventURI(response.toInt())
                    //remove from calender
                    AppUtils.deleteCalenderEvent(strEventURI, activity!!)

                    //deleting all notifications
                    val notificationDAO =
                        MhealthRoomDatabase.getAppDataBase(activity!!)!!.notificationDAO()
                    val lstReminder = notificationDAO.getNotificationByReminderItemId(
                        response.toInt(),
                        Constant.NOTIFICATION_TYPE_APPOINTMENT
                    )
                    for (i in 0 until lstReminder.size) {
                        AppUtils.deleteScheduledNotification(UUID.fromString(lstReminder.get(i).notificationReqId))
                    }

                    appointmentReminderDAO.deleteAppointmentReminder(response.toInt())
                    setupAppointmentUI()
                }
            }
        }
        val adapter = AppointmentReminderAdapter(activity!!, delListener)
        lstAppointmentReminder.adapter = adapter
        adapter.setAppointmnetList(listAppointments)

        if (isAppointsReminderEmpty) {
            cardAppointsReminderEmpty.visibility = View.VISIBLE
            lstAppointmentReminder.visibility = View.GONE
        } else {
            cardAppointsReminderEmpty.visibility = View.GONE
            lstAppointmentReminder.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    fun setPeriodDetails() {
        val lastPeriod = periodTrackerDAO.getLastPeriod()
        //setting button text [Start period / End period]
        if (TextUtils.isEmpty(lastPeriod.endDate))
            btnLogPeriod.text = activity!!.getString(R.string.EndPeriod)
        else
            btnLogPeriod.text = activity!!.getString(R.string.StartPeriod)

        //setting next period days
        var totalLength = 0
        for (i in 0 until periodList.size) {
            totalLength += periodList.get(i).avgCycle
        }
        val avgPeriodLength = totalLength / periodList.size
        val nextPeriodDays = AppUtils.getPeriodDays(lastPeriod.startDate, avgPeriodLength)
        txtNextPeriodDays.text = nextPeriodDays.toString()
        //setting last period duration
        txtValLastPeriodDuration.text =
            lastPeriod.periodLength.toString() + activity!!.getString(R.string.days)//" Days"
        if (nextPeriodDays < 0) {
            showForgotPeriodPopup()

        }
    }

    fun showForgotPeriodPopup() {
        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        if (dialog != null) {
            dialog.setCancelable(false)
        }
        if (dialog != null) {
            dialog.setContentView(R.layout.dialog_delete_reminder)
        }
        if (dialog != null) {
            dialog.txtHeader.text = activity!!.getString(R.string.PeriodsOverDue)
        }
        if (dialog != null) {
            dialog.txtMessage.text = activity!!.getString(R.string.megForgotPeriodLog)
        }
        if (dialog != null) {
            dialog.txtNo.setOnClickListener {
                dialog.dismiss()
                forgotAddPeriod = false
            }
        }

        if (dialog != null) {
            dialog.txtYes.setOnClickListener {
                dialog.dismiss()
                forgotAddPeriod = true
                btnLogPeriod.performClick()
            }
        }
        if (dialog != null) {
            dialog.show()
        }
    }

    fun showEndDatePopup(message: String) {
        var enteredDt: String = ""
        val lastPeriod = periodTrackerDAO.getLastPeriod()
        //setting min and max dates according to last start date
        val sdf = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
        val ca1LastStartDt = Calendar.getInstance()
        val calToday = Calendar.getInstance()
        ca1LastStartDt.time = sdf.parse(lastPeriod.startDate)
        if (calToday.before(ca1LastStartDt)) {//if last period date is future date
            Toast.makeText(activity, activity!!.getString(R.string.PeriodsNotStarted), Toast.LENGTH_LONG).show()
        } else {
            val dialog = activity?.let { Dialog(it) }
            if (dialog != null) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.popup_period_date_input)
                dialog.btnDone.setOnClickListener {
                    dialog.dismiss()
                    //Update data in DB
                    val periodLength = AppUtils.getDaysBetween(lastPeriod.startDate, enteredDt)
                    periodTrackerDAO.upDateEndDate(enteredDt, periodLength.toInt(), lastPeriod.periodId)
                    setupPeriodTrackingUI()
                }
                dialog.txtHeader.text = message
                dialog.calendarView.minDate = ca1LastStartDt.timeInMillis
            }

            ca1LastStartDt.add(Calendar.MONTH, 1)

            if (dialog != null) {
                dialog.calendarView.maxDate = ca1LastStartDt.timeInMillis
                dialog.calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
                    // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
                    enteredDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                }
                dialog.txtClose.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }

        }

    }

    fun showStartDatePopup(message: String) {
        var enteredDt: String = ""

        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.popup_period_date_input)
            dialog.btnDone.setOnClickListener {
                dialog.dismiss()
                //Update data in DB
                val lastPeriod = periodTrackerDAO.getLastPeriod()
                val avgCycleLength = AppUtils.getDaysBetween(lastPeriod.startDate, enteredDt)
                periodTrackerDAO.insert(PeriodTracker(enteredDt, "", lastPeriod.periodLength, avgCycleLength.toInt()))
                setupPeriodTrackingUI()
            }
            dialog.txtHeader.text = message
        }


        val lastPeriod = periodTrackerDAO.getLastPeriod()
        //setting min and max dates according to last end date
        val sdf = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
        val ca1LastEndDt = Calendar.getInstance()
        ca1LastEndDt.time = sdf.parse(lastPeriod.endDate)
        if (dialog != null) {
            dialog.calendarView?.minDate = ca1LastEndDt.timeInMillis
            dialog.calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
                // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
                enteredDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
            }
            dialog.txtClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    override fun onComplete(response: String) {
        if (response.equals("add")) {
            val intent = Intent(activity, MedicineReminderInputActivity::class.java)
            startActivityForResult(intent, REQ_CODE_MEDICINE_REMINDER)
        } else {//remove reminder from medicine reminder list
            val medicineReminderDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!)!!.medicineReminderDAO()
            medicineReminderDAO.deleteMedicineReminder(response.toInt())
            //remove all reminders from calender
            val medicineEventDAO = MhealthRoomDatabase.getAppDataBase(this.activity!!)!!.medicineEventDAO()
            val medicineEventList = medicineEventDAO.getAllEventByReminderId(response.toInt())
            for (i in 0 until medicineEventList.size) {
                AppUtils.deleteCalenderEvent(medicineEventList.get(i).eventURI, this!!.activity!!)
            }
            //deleting all notifications
            val notificationDAO =
                MhealthRoomDatabase.getAppDataBase(activity!!)!!.notificationDAO()
            val lstReminder =
                notificationDAO.getNotificationByReminderItemId(response.toInt(), Constant.NOTIFICATION_TYPE_MEDICINE)
            for (i in 0 until lstReminder.size) {
                AppUtils.deleteScheduledNotification(UUID.fromString(lstReminder.get(i).notificationReqId))
            }

            setMedicineReminderUI()
        }
    }
}