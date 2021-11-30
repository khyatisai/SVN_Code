package com.unfpa.appsistenciamaternaunfpa.utils

import androidx.fragment.app.FragmentActivity
import com.unfpa.appsistenciamaternaunfpa.R
import java.util.*
import android.text.TextUtils
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import android.provider.CalendarContract.Reminders
import android.provider.CalendarContract.Events
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.work.*
import com.android.volley.*
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.Mhealth
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.TypeForCountry
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.AppointmentReminder
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineEvent
import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification
import com.unfpa.appsistenciamaternaunfpa.utils.notification.AlarmReceiver
import com.unfpa.appsistenciamaternaunfpa.utils.notification.OpenAppNotifyWorker
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException


class AppUtils {
    companion object {

        fun addFragment(
            activity: androidx.fragment.app.FragmentActivity,
            fragment: androidx.fragment.app.Fragment,
            addToBackStack: Boolean,
            tag: String
        ) {
            val manager = activity.supportFragmentManager
            val ft = manager.beginTransaction()

            if (addToBackStack) {
                ft.addToBackStack(tag)
            }
            //ft.setCustomAnimations(R.animator.slide_up, 0, 0, R.animator.slide_down)
            ft.setCustomAnimations(
                R.animator.fragment_animation_fade_in,
                R.animator.fragment_animation_fade_out
            )
            ft.replace(com.unfpa.appsistenciamaternaunfpa.R.id.frame_content, fragment, tag)
            ft.commitAllowingStateLoss()
        }

        fun clearbackStackforFrag(
            strFragName: String,
            activity: androidx.fragment.app.FragmentActivity
        ) {
            val manager = activity.supportFragmentManager
            manager.popBackStack(
                strFragName,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

//        fun getThubnailURL(inUrl: String): String {
//            val newInUrl = inUrl.replace("&feature=youtu.be", "")
//            if (newInUrl.toLowerCase().contains("youtu.be")) {
//                return newInUrl.substring(inUrl.lastIndexOf("/") + 1)
//            }
//            val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
//            val compiledPattern = Pattern.compile(pattern)
//            val matcher = compiledPattern.matcher(inUrl)
//            return if (matcher.find()) {
//                "http://img.youtube.com/vi/" + matcher.group() + "/0.jpg"
//            } else ""
//        }


        fun clearBackstack(activity: androidx.fragment.app.FragmentActivity) {
            val manager = activity.supportFragmentManager
            manager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }


        fun getUUID(context: Context): String {
            var uniqueID: String = ""

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            uniqueID = sharedPrefs.getString(Constant.PREF_UNIQUE_ID, "").toString()
            if (uniqueID == null || TextUtils.isEmpty(uniqueID)) {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(Constant.PREF_UNIQUE_ID, uniqueID)
                editor.commit()
            }
            return uniqueID
        }

        fun getDisplayName(context: Context): String {
            var dispName: String = ""

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            dispName = sharedPrefs.getString(Constant.PREF_CHAT_DISP_NAME, "").toString()

            return dispName
        }

        fun setDisplayName(context: Context, strDisplayName: String) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(Constant.PREF_CHAT_DISP_NAME, strDisplayName)
            editor.commit()
        }

        fun setCountryOffice(context: Context, strCoCode: String, strCoShortCode: String) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(Constant.PREF_CO_CODE, strCoCode)
            editor.putString(Constant.ITEM_COUNTRY_CODE, strCoShortCode)
            editor.commit()
        }

        fun getCoShortCode(context: Context): String {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPrefs.getString(Constant.PREF_CO_CODE, "").toString()
        }

        fun setCallTime(context: Context, time: String, tag: String) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(tag, time)
            editor.commit()
        }

        fun getCallTime(context: Context, tag: String): String {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPrefs.getString(tag, "").toString()
        }

        fun getCountryOffice(context: Context): String {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPrefs.getString(Constant.PREF_CO_CODE, "").toString()
        }

        fun setLanguageCode(context: Context, strLanguageCode: String) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(Constant.PREF_LANGUAGE_CODE, strLanguageCode)
            editor.commit()
        }

        fun getLanguageCode(context: Context): String {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPrefs.getString(Constant.PREF_LANGUAGE_CODE, "").toString()
        }

        fun setIsFirstTime(context: Context, isFirstTime: Boolean) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putBoolean(Constant.PREF_IS_FIRST_TIME, isFirstTime)
            editor.commit()
        }

        fun getIsFirstTime(context: Context): Boolean {

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPrefs.getBoolean(Constant.PREF_IS_FIRST_TIME, true)

        }

        fun getStoredTimestemp(context: Context): String {

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPrefs.getString(Constant.SERVER_TIMESTEMP, "").toString()
        }

        fun setTimestemp(context: Context, strTimestemp: String) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(Constant.SERVER_TIMESTEMP, strTimestemp)
            editor.commit()
        }

        fun getPeriodDays(strLastStratDate: String, avgCycleLength: Int): Long {
            var daysDiff = 0L
            try {

                val sdf = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                val ca1LastStartDt = Calendar.getInstance()
                val calToday = Calendar.getInstance()
                ca1LastStartDt.time = sdf.parse(strLastStratDate)
                //if user has entered future start date
                val diffDates = calToday.timeInMillis - ca1LastStartDt.timeInMillis
                if (diffDates < 0) { //user has entered future date for period
                    val msDiff = ca1LastStartDt.timeInMillis - calToday.getTimeInMillis()
                    daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
                } else {
                    ca1LastStartDt.add(
                        Calendar.DATE,
                        avgCycleLength
                    )  // number of days to add, to get future period date
                    val msDiff = ca1LastStartDt.timeInMillis - calToday.getTimeInMillis()
                    daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return daysDiff
        }


        fun getDaysBetween(strDate1: String, strDate2: String): Long {
            var daysDiff = 0L
            try {
                val sdf = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                val c1 = Calendar.getInstance()
                val c = Calendar.getInstance()
                c1.time = sdf.parse(strDate1)
                c.time = sdf.parse(strDate2)

                val msDiff = c.timeInMillis - c1.getTimeInMillis()
                daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return daysDiff
        }

        fun getTime12HourFormat(strTime24Hour: String): String {
            try {
                val sdf = SimpleDateFormat(Constant.MEDICINE_TIME_FORMAT_INPUT)
                val sdfTarget = SimpleDateFormat(Constant.MEDICINE_TIME_FORMAT_TARGET)
                val time = sdf.parse(strTime24Hour)
                return sdfTarget.format(time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }


        fun addAppointmentReminder(
            appointmentReminder: AppointmentReminder,
            activity: Activity
//            activity: androidx.fragment.app.FragmentActivity
        ) {
            val cr = activity.getContentResolver()
            val calStartDate = Calendar.getInstance()
            val inputDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
            val date = inputDateFormat.parse(appointmentReminder.date)
            calStartDate.time = date
            calStartDate.set(
                Calendar.HOUR_OF_DAY,
                appointmentReminder.time.split(":").get(0).toInt()
            );
            calStartDate.set(Calendar.MINUTE, appointmentReminder.time.split(":").get(1).toInt());

            val values = ContentValues()
            values.put(Events.DTSTART, calStartDate.timeInMillis)
            values.put(Events.DTEND, calStartDate.timeInMillis)
            values.put(Events.TITLE, appointmentReminder.appointmentName)
            values.put(Events.DESCRIPTION, appointmentReminder.serviceCenterName)
            values.put(Events.CALENDAR_ID, 1);
            //Adding alarm before 60 mins
            values.put(Events.HAS_ALARM, true)
            //Get current timezone
            values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            val uri = cr.insert(Events.CONTENT_URI, values)
            Log.i("mHealth", "Uri returned=>" + uri!!.toString())
            // get the event ID that is the last element in the Uri
            val eventID = java.lang.Long.parseLong(uri!!.getLastPathSegment().toString())
            val reminders = ContentValues()
            reminders.put(Reminders.EVENT_ID, eventID)
            reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT)
            reminders.put(Reminders.MINUTES, 60) //reminder before 60 mins
            val uri2 = cr.insert(Reminders.CONTENT_URI, reminders)
            //update event URI in DB
            //add appointment to DB
            val appointmentReminderDAO =
                MhealthRoomDatabase.getAppDataBase(activity!!)!!.appointmentReminderDAO()
            appointmentReminderDAO.updateEventId(uri.toString(), appointmentReminder.appointmentId)
        }

        fun addCalenderReminderMedicine(
            medicineReminder: MedicineReminder,
            activity: androidx.fragment.app.FragmentActivity
        ) {
            try {
                val cr = activity.getContentResolver()
                val calStartDate = Calendar.getInstance()
                val inputDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                val date = inputDateFormat.parse(medicineReminder.date)
                calStartDate.time = date
                calStartDate.set(
                    Calendar.HOUR_OF_DAY,
                    medicineReminder.time.split(":").get(0).toInt()
                );
                calStartDate.set(Calendar.MINUTE, medicineReminder.time.split(":").get(1).toInt());
                for (i in 0 until medicineReminder.days) {

                    val values = ContentValues()
                    values.put(Events.DTSTART, calStartDate.timeInMillis)
                    /* calStartDate.add(
                         Calendar.,
                         medicineReminder.days
                     )*/
                    values.put(Events.DTEND, calStartDate.timeInMillis)
                    values.put(Events.TITLE, medicineReminder.medicineName)
                    values.put(Events.DESCRIPTION, medicineReminder.dose)
                    values.put(Events.CALENDAR_ID, 1);
                    /*if (allDay === 1) {
                    values.put(Events.ALL_DAY, true)
                }*/

                    if (!TextUtils.isEmpty(medicineReminder.reminderBefore)) {
                        values.put(Events.HAS_ALARM, true)
                    }

                    //Get current timezone
                    values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                    val uri = cr.insert(Events.CONTENT_URI, values)
                    Log.i("mHealth", "Uri returned=>" + uri!!.toString())
                    // get the event ID that is the last element in the Uri
                    val eventID = java.lang.Long.parseLong(uri!!.getLastPathSegment().toString())

                    if (!TextUtils.isEmpty(medicineReminder.reminderBefore)) {
                        val reminders = ContentValues()
                        reminders.put(Reminders.EVENT_ID, eventID)
                        reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT)
                        var selectedReminderValue = 0
                        if (medicineReminder.reminderBefore.equals("15 mins"))
                            selectedReminderValue = 15
                        else if (medicineReminder.reminderBefore.equals("30 mins"))
                            selectedReminderValue = 30
                        else if (medicineReminder.reminderBefore.equals("1 hour"))
                            selectedReminderValue = 60
                        else if (medicineReminder.reminderBefore.equals("2 hour"))
                            selectedReminderValue = 120

                        reminders.put(Reminders.MINUTES, selectedReminderValue)

                        val uri2 = cr.insert(Reminders.CONTENT_URI, reminders)
                    }
                    val medicineEventDAO =
                        MhealthRoomDatabase.getAppDataBase(activity)!!.medicineEventDAO()
                    medicineEventDAO.insert(
                        MedicineEvent(
                            medicineReminder.medicineReminderId,
                            eventID,
                            uri.toString()
                        )
                    )
                    calStartDate.add(
                        Calendar.DATE,
                        1
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun deleteCalenderEvent(
            eventUri: String,
            activity: androidx.fragment.app.FragmentActivity
        ) {
            try {
                /*val eventsUri = Uri.parse (activity.getCalendarUriBase() + "events");
            Uri eventUri = ContentUris . withAppendedId (eventsUri, entryID);*/
                val iNumRowsDeleted =
                    activity.getContentResolver().delete(Uri.parse(eventUri), null, null);
                Log.i("mHealth", "Deleted " + iNumRowsDeleted + " calendar entry.");
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun trackScreen(name: String, activity: FragmentActivity) {
            Mhealth.firebaseAnalytics.setCurrentScreen(activity, name, name)
        }

        fun isConnectingToInternet(context: Context): Boolean {
            val connectivity = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info)
                        if (i.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
            }
            return false
        }

        fun wipeAllDataForSync(context: Context) {
            try {
                MhealthRoomDatabase.getAppDataBase(context!!)!!.countryOfficeDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(context!!)!!.myServiceDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(context!!)!!.serviceCenterDetailDAO()
                MhealthRoomDatabase.getAppDataBase(context!!)!!.SRHContentCategoryDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(context!!)!!.contentMasterDAO().deleteAll()
                MhealthRoomDatabase.getAppDataBase(context)!!.quizRequestDAO().deleteAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getChatUID(context: Context): String {
            var uniqueID: String = ""

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            uniqueID = sharedPrefs.getString(Constant.PREF_CHAT_UID, "").toString()
            return uniqueID
        }

        fun setChatUID(context: Context, uId: String) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(Constant.PREF_CHAT_UID, uId)
            editor.commit()
        }

        fun hideKeyboardFrom(context: Context, view: View) {
            val imm =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as (InputMethodManager);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        fun dateConverter(sourceFormat: String, targetFormat: String, strDate: String): String {
            val format1 = SimpleDateFormat(sourceFormat)
            val format2 = SimpleDateFormat(targetFormat)
            val date = format1.parse(strDate)
            return (format2.format(date))
        }

        //Using alrm manager
        fun setNotification(timeInMilliSeconds: Long, activity: Activity) {

            //------------  alarm settings start  -----------------//

            if (timeInMilliSeconds > 0) {


                val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(
                    activity.applicationContext,
                    AlarmReceiver::class.java
                ) // AlarmReceiver1 = broadcast receiver

                alarmIntent.putExtra("reason", "notification")
                alarmIntent.putExtra("timestamp", timeInMilliSeconds)


                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timeInMilliSeconds


                val pendingIntent =
                    PendingIntent.getBroadcast(
                        activity,
                        0,
                        alarmIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            }

            //------------ end of alarm settings  -----------------//


        }

        @SuppressLint("RestrictedApi")
        fun setScheduledNotificationMedicine(medicineReminder: MedicineReminder, context: Context) {
            try {
                val currentTime = System.currentTimeMillis()
                val medicineTime = Calendar.getInstance()
                val inputDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                medicineTime.time = inputDateFormat.parse(medicineReminder.date)
                medicineTime.set(
                    Calendar.HOUR_OF_DAY,
                    medicineReminder.time.split(":").get(0).toInt()
                );
                medicineTime.set(Calendar.MINUTE, medicineReminder.time.split(":").get(1).toInt());

                for (i in 0 until medicineReminder.days) {
                    //Adding notification in DB
                    val notificationDAO =
                        MhealthRoomDatabase.getAppDataBase(context)!!.notificationDAO()
                    var foodStr = ""
                    if (medicineReminder.withFood) {
                        foodStr = "with food"
                    } else {
                        foodStr = "without food"
                    }
                    val strMedicineTime = AppUtils.getTime12HourFormat(medicineReminder.time)
                    val notification = Notification(
                        medicineReminder.medicineName,
                        medicineReminder.dose.toString() + " dose at " + strMedicineTime,
                        foodStr,
                        medicineReminder.time,
                        Constant.NOTIFICATION_TYPE_MEDICINE,
                        "",
                        false,
                        medicineReminder.medicineReminderId
                    )
                    val notificationId = notificationDAO.insert(notification)


                    val d = Data.Builder()
                        .putInt(Constant.NOTIFICATION_ITEM_ID, medicineReminder.medicineReminderId)
                        .putInt(Constant.NOTIFICATION_ID, notificationId.toInt())
                        .build()

                    val specificTimeToTrigger = medicineTime.getTimeInMillis()
                    val delayToPass =
                        specificTimeToTrigger - currentTime - 900000L //(Default Reminder before 15 mins)
                    //update notification time
                    val notificationTime = medicineTime.getTimeInMillis() - 900000L
                    notificationDAO.updateNotificationTime(
                        notificationId.toInt(),
                        notificationTime.toString()
                    )
                    val oneTimeWorkRequest = OneTimeWorkRequestBuilder<OpenAppNotifyWorker>()
                        .setInputData(d)
                        .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
                        .build()
                    WorkManager.getInstance().enqueue(oneTimeWorkRequest)
                    notificationDAO.updateNotificationReqId(
                        notificationId.toInt(),
                        oneTimeWorkRequest.id.toString()
                    )
                    medicineTime.add(
                        Calendar.DATE,
                        1
                    )
                    /*val oneTimeWorkRequest = OneTimeWorkRequestBuilder<OpenAppNotifyWorker>()
                            .setInitialDelay(30000, TimeUnit.MILLISECONDS)
                            .build()
                        WorkManager.getInstance().enqueue(oneTimeWorkRequest)*/
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun setScheduledNotificationAppointment(
            appointmentReminder: AppointmentReminder,
            context: Context
        ) {
            try {
                val currentTime = System.currentTimeMillis()
                val calStartDate = Calendar.getInstance()
                val inputDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                val date = inputDateFormat.parse(appointmentReminder.date)
                calStartDate.time = date
                calStartDate.set(
                    Calendar.HOUR_OF_DAY,
                    appointmentReminder.time.split(":").get(0).toInt()
                );
                calStartDate.set(
                    Calendar.MINUTE,
                    appointmentReminder.time.split(":").get(1).toInt()
                );

                //Adding notification in DB
                val notificationDAO =
                    MhealthRoomDatabase.getAppDataBase(context)!!.notificationDAO()


                val notification = Notification(
                    appointmentReminder.appointmentName,
                    appointmentReminder.date + "  " + AppUtils.getTime12HourFormat(
                        appointmentReminder.time
                    ),
                    appointmentReminder.serviceCenterName,
                    appointmentReminder.time,
                    Constant.NOTIFICATION_TYPE_APPOINTMENT,
                    "",
                    false,
                    appointmentReminder.appointmentId
                )
                val notificationId = notificationDAO.insert(notification)


                val d = Data.Builder()
                    .putInt(Constant.NOTIFICATION_ITEM_ID, appointmentReminder.appointmentId)
                    .putInt(Constant.NOTIFICATION_ID, notificationId.toInt())
                    .build()

                val specificTimeToTrigger = calStartDate.getTimeInMillis()
                val delayToPass =
                    specificTimeToTrigger - currentTime - (60000L * 60)//(Default Reminder before 60 mins)
                //update notification time
                val notificationTime = calStartDate.getTimeInMillis() - (60000L * 60)
                notificationDAO.updateNotificationTime(
                    notificationId.toInt(),
                    notificationTime.toString()
                )
                val oneTimeWorkRequest = OneTimeWorkRequestBuilder<OpenAppNotifyWorker>()
                    .setInputData(d)
                    .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
                    .build()
                WorkManager.getInstance().enqueue(oneTimeWorkRequest)
                notificationDAO.updateNotificationReqId(
                    notificationId.toInt(),
                    oneTimeWorkRequest.id.toString()
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun deleteScheduledNotification(notificationReqId: UUID) {
            try {
                WorkManager.getInstance().cancelWorkById(notificationReqId);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getCurrentTimestamp(): Long {
            val time = System.currentTimeMillis()
            return time
        }

        fun getCountryList(): MutableList<TypeForCountry>? {
            var countrylist: MutableList<TypeForCountry>? = ArrayList<TypeForCountry>()
            val str =
                "[{\"Code\":\"AUS\",\"Name\":\"Australia\"},{\"Code\":\"UAE\",\"Name\":\"United Arab Emirates\"},{\"Code\":\"UK\",\"Name\":\"United Kingdom\"},{\"Code\":\"USA\",\"Name\":\"United States Of America\"},{\"Code\":\"IND\",\"Name\":\"India\"},{\"Code\":\"AD\",\"Name\":\"Andorra\"},{\"Code\":\"AO\",\"Name\":\"Angola\"},{\"Code\":\"AI\",\"Name\":\"Anguilla\"},{\"Code\":\"AQ\",\"Name\":\"Antarctica\"},{\"Code\":\"AG\",\"Name\":\"Antigua and Barbuda\"},{\"Code\":\"AR\",\"Name\":\"Argentina\"},{\"Code\":\"AM\",\"Name\":\"Armenia\"},{\"Code\":\"AW\",\"Name\":\"Aruba\"},{\"Code\":\"AU\",\"Name\":\"Australia\"},{\"Code\":\"AT\",\"Name\":\"Austria\"},{\"Code\":\"AZ\",\"Name\":\"Azerbaijan\"},{\"Code\":\"BS\",\"Name\":\"Bahamas\"},{\"Code\":\"BH\",\"Name\":\"Bahrain\"},{\"Code\":\"BD\",\"Name\":\"Bangladesh\"},{\"Code\":\"BB\",\"Name\":\"Barbados\"},{\"Code\":\"BY\",\"Name\":\"Belarus\"},{\"Code\":\"BE\",\"Name\":\"Belgium\"},{\"Code\":\"BZ\",\"Name\":\"Belize\"},{\"Code\":\"BJ\",\"Name\":\"Benin\"},{\"Code\":\"BM\",\"Name\":\"Bermuda\"},{\"Code\":\"BT\",\"Name\":\"Bhutan\"},{\"Code\":\"BO\",\"Name\":\"Bolivia, Plurinational State of\"},{\"Code\":\"BQ\",\"Name\":\"Bonaire, Sint Eustatius and Saba\"},{\"Code\":\"BA\",\"Name\":\"Bosnia and Herzegovina\"},{\"Code\":\"BW\",\"Name\":\"Botswana\"},{\"Code\":\"BV\",\"Name\":\"Bouvet Island\"},{\"Code\":\"BR\",\"Name\":\"Brazil\"},{\"Code\":\"IO\",\"Name\":\"British Indian Ocean Territory\"},{\"Code\":\"BN\",\"Name\":\"Brunei Darussalam\"},{\"Code\":\"BG\",\"Name\":\"Bulgaria\"},{\"Code\":\"BF\",\"Name\":\"Burkina Faso\"},{\"Code\":\"BI\",\"Name\":\"Burundi\"},{\"Code\":\"KH\",\"Name\":\"Cambodia\"},{\"Code\":\"CM\",\"Name\":\"Cameroon\"},{\"Code\":\"CA\",\"Name\":\"Canada\"},{\"Code\":\"CV\",\"Name\":\"Cape Verde\"},{\"Code\":\"KY\",\"Name\":\"Cayman Islands\"},{\"Code\":\"CF\",\"Name\":\"Central African Republic\"},{\"Code\":\"TD\",\"Name\":\"Chad\"},{\"Code\":\"CL\",\"Name\":\"Chile\"},{\"Code\":\"CN\",\"Name\":\"China\"},{\"Code\":\"CX\",\"Name\":\"Christmas Island\"},{\"Code\":\"CC\",\"Name\":\"Cocos (Keeling) Islands\"},{\"Code\":\"CO\",\"Name\":\"Colombia\"},{\"Code\":\"KM\",\"Name\":\"Comoros\"},{\"Code\":\"CG\",\"Name\":\"Congo\"},{\"Code\":\"CD\",\"Name\":\"Congo, the Democratic Republic of the\"},{\"Code\":\"CK\",\"Name\":\"Cook Islands\"},{\"Code\":\"CR\",\"Name\":\"Costa Rica\"},{\"Code\":\"CI\",\"Name\":\"C\\u00f4te d'Ivoire\"},{\"Code\":\"HR\",\"Name\":\"Croatia\"},{\"Code\":\"CU\",\"Name\":\"Cuba\"},{\"Code\":\"CW\",\"Name\":\"Cura\\u00e7ao\"},{\"Code\":\"CY\",\"Name\":\"Cyprus\"},{\"Code\":\"CZ\",\"Name\":\"Czech Republic\"},{\"Code\":\"DK\",\"Name\":\"Denmark\"},{\"Code\":\"DJ\",\"Name\":\"Djibouti\"},{\"Code\":\"DM\",\"Name\":\"Dominica\"},{\"Code\":\"DO\",\"Name\":\"Dominican Republic\"},{\"Code\":\"EC\",\"Name\":\"Ecuador\"},{\"Code\":\"EG\",\"Name\":\"Egypt\"},{\"Code\":\"SV\",\"Name\":\"El Salvador\"},{\"Code\":\"GQ\",\"Name\":\"Equatorial Guinea\"},{\"Code\":\"ER\",\"Name\":\"Eritrea\"},{\"Code\":\"EE\",\"Name\":\"Estonia\"},{\"Code\":\"ET\",\"Name\":\"Ethiopia\"},{\"Code\":\"FK\",\"Name\":\"Falkland Islands (Malvinas)\"},{\"Code\":\"FO\",\"Name\":\"Faroe Islands\"},{\"Code\":\"FJ\",\"Name\":\"Fiji\"},{\"Code\":\"FI\",\"Name\":\"Finland\"},{\"Code\":\"FR\",\"Name\":\"France\"},{\"Code\":\"GF\",\"Name\":\"French Guiana\"},{\"Code\":\"PF\",\"Name\":\"French Polynesia\"},{\"Code\":\"TF\",\"Name\":\"French Southern Territories\"},{\"Code\":\"GA\",\"Name\":\"Gabon\"},{\"Code\":\"GM\",\"Name\":\"Gambia\"},{\"Code\":\"GE\",\"Name\":\"Georgia\"},{\"Code\":\"DE\",\"Name\":\"Germany\"},{\"Code\":\"GH\",\"Name\":\"Ghana\"},{\"Code\":\"GI\",\"Name\":\"Gibraltar\"},{\"Code\":\"GR\",\"Name\":\"Greece\"},{\"Code\":\"GL\",\"Name\":\"Greenland\"},{\"Code\":\"GD\",\"Name\":\"Grenada\"},{\"Code\":\"GP\",\"Name\":\"Guadeloupe\"},{\"Code\":\"GU\",\"Name\":\"Guam\"},{\"Code\":\"GT\",\"Name\":\"Guatemala\"},{\"Code\":\"GG\",\"Name\":\"Guernsey\"},{\"Code\":\"GN\",\"Name\":\"Guinea\"},{\"Code\":\"GW\",\"Name\":\"Guinea-Bissau\"},{\"Code\":\"GY\",\"Name\":\"Guyana\"},{\"Code\":\"HT\",\"Name\":\"Haiti\"},{\"Code\":\"HM\",\"Name\":\"Heard Island and McDonald Islands\"},{\"Code\":\"VA\",\"Name\":\"Holy See (Vatican City State)\"},{\"Code\":\"HN\",\"Name\":\"Honduras\"},{\"Code\":\"HK\",\"Name\":\"Hong Kong\"},{\"Code\":\"HU\",\"Name\":\"Hungary\"},{\"Code\":\"IS\",\"Name\":\"Iceland\"},{\"Code\":\"IN\",\"Name\":\"India\"},{\"Code\":\"ID\",\"Name\":\"Indonesia\"},{\"Code\":\"IR\",\"Name\":\"Iran, Islamic Republic of\"},{\"Code\":\"IQ\",\"Name\":\"Iraq\"},{\"Code\":\"IE\",\"Name\":\"Ireland\"},{\"Code\":\"IM\",\"Name\":\"Isle of Man\"},{\"Code\":\"IL\",\"Name\":\"Israel\"},{\"Code\":\"IT\",\"Name\":\"Italy\"},{\"Code\":\"JM\",\"Name\":\"Jamaica\"},{\"Code\":\"JP\",\"Name\":\"Japan\"},{\"Code\":\"JE\",\"Name\":\"Jersey\"},{\"Code\":\"JO\",\"Name\":\"Jordan\"},{\"Code\":\"KZ\",\"Name\":\"Kazakhstan\"},{\"Code\":\"KE\",\"Name\":\"Kenya\"},{\"Code\":\"KI\",\"Name\":\"Kiribati\"},{\"Code\":\"KP\",\"Name\":\"Korea, Democratic People's Republic of\"},{\"Code\":\"KR\",\"Name\":\"Korea, Republic of\"},{\"Code\":\"KW\",\"Name\":\"Kuwait\"},{\"Code\":\"KG\",\"Name\":\"Kyrgyzstan\"},{\"Code\":\"LA\",\"Name\":\"Lao People's Democratic Republic\"},{\"Code\":\"LV\",\"Name\":\"Latvia\"},{\"Code\":\"LB\",\"Name\":\"Lebanon\"},{\"Code\":\"LS\",\"Name\":\"Lesotho\"},{\"Code\":\"LR\",\"Name\":\"Liberia\"},{\"Code\":\"LY\",\"Name\":\"Libya\"},{\"Code\":\"LI\",\"Name\":\"Liechtenstein\"},{\"Code\":\"LT\",\"Name\":\"Lithuania\"},{\"Code\":\"LU\",\"Name\":\"Luxembourg\"},{\"Code\":\"MO\",\"Name\":\"Macao\"},{\"Code\":\"MK\",\"Name\":\"Macedonia, the Former Yugoslav Republic of\"},{\"Code\":\"MG\",\"Name\":\"Madagascar\"},{\"Code\":\"MW\",\"Name\":\"Malawi\"},{\"Code\":\"MY\",\"Name\":\"Malaysia\"},{\"Code\":\"MV\",\"Name\":\"Maldives\"},{\"Code\":\"ML\",\"Name\":\"Mali\"},{\"Code\":\"MT\",\"Name\":\"Malta\"},{\"Code\":\"MH\",\"Name\":\"Marshall Islands\"},{\"Code\":\"MQ\",\"Name\":\"Martinique\"},{\"Code\":\"MR\",\"Name\":\"Mauritania\"},{\"Code\":\"MU\",\"Name\":\"Mauritius\"},{\"Code\":\"YT\",\"Name\":\"Mayotte\"},{\"Code\":\"MX\",\"Name\":\"Mexico\"},{\"Code\":\"FM\",\"Name\":\"Micronesia, Federated States of\"},{\"Code\":\"MD\",\"Name\":\"Moldova, Republic of\"},{\"Code\":\"MC\",\"Name\":\"Monaco\"},{\"Code\":\"MN\",\"Name\":\"Mongolia\"},{\"Code\":\"ME\",\"Name\":\"Montenegro\"},{\"Code\":\"MS\",\"Name\":\"Montserrat\"},{\"Code\":\"MA\",\"Name\":\"Morocco\"},{\"Code\":\"MZ\",\"Name\":\"Mozambique\"},{\"Code\":\"MM\",\"Name\":\"Myanmar\"},{\"Code\":\"NA\",\"Name\":\"Namibia\"},{\"Code\":\"NR\",\"Name\":\"Nauru\"},{\"Code\":\"NP\",\"Name\":\"Nepal\"},{\"Code\":\"NL\",\"Name\":\"Netherlands\"},{\"Code\":\"NC\",\"Name\":\"New Caledonia\"},{\"Code\":\"NZ\",\"Name\":\"New Zealand\"},{\"Code\":\"NI\",\"Name\":\"Nicaragua\"},{\"Code\":\"NE\",\"Name\":\"Niger\"},{\"Code\":\"NG\",\"Name\":\"Nigeria\"},{\"Code\":\"NU\",\"Name\":\"Niue\"},{\"Code\":\"NF\",\"Name\":\"Norfolk Island\"},{\"Code\":\"MP\",\"Name\":\"Northern Mariana Islands\"},{\"Code\":\"NO\",\"Name\":\"Norway\"},{\"Code\":\"OM\",\"Name\":\"Oman\"},{\"Code\":\"PK\",\"Name\":\"Pakistan\"},{\"Code\":\"PW\",\"Name\":\"Palau\"},{\"Code\":\"PS\",\"Name\":\"Palestine, State of\"},{\"Code\":\"PA\",\"Name\":\"Panama\"},{\"Code\":\"PG\",\"Name\":\"Papua New Guinea\"},{\"Code\":\"PY\",\"Name\":\"Paraguay\"},{\"Code\":\"PE\",\"Name\":\"Peru\"},{\"Code\":\"PH\",\"Name\":\"Philippines\"},{\"Code\":\"PN\",\"Name\":\"Pitcairn\"},{\"Code\":\"PL\",\"Name\":\"Poland\"},{\"Code\":\"PT\",\"Name\":\"Portugal\"},{\"Code\":\"PR\",\"Name\":\"Puerto Rico\"},{\"Code\":\"QA\",\"Name\":\"Qatar\"},{\"Code\":\"RE\",\"Name\":\"R\\u00e9union\"},{\"Code\":\"RO\",\"Name\":\"Romania\"},{\"Code\":\"RU\",\"Name\":\"Russian Federation\"},{\"Code\":\"RW\",\"Name\":\"Rwanda\"},{\"Code\":\"BL\",\"Name\":\"Saint Barth\\u00e9lemy\"},{\"Code\":\"SH\",\"Name\":\"Saint Helena, Ascension and Tristan da Cunha\"},{\"Code\":\"KN\",\"Name\":\"Saint Kitts and Nevis\"},{\"Code\":\"LC\",\"Name\":\"Saint Lucia\"},{\"Code\":\"MF\",\"Name\":\"Saint Martin (French part)\"},{\"Code\":\"PM\",\"Name\":\"Saint Pierre and Miquelon\"},{\"Code\":\"VC\",\"Name\":\"Saint Vincent and the Grenadines\"},{\"Code\":\"WS\",\"Name\":\"Samoa\"},{\"Code\":\"SM\",\"Name\":\"San Marino\"},{\"Code\":\"ST\",\"Name\":\"Sao Tome and Principe\"},{\"Code\":\"SA\",\"Name\":\"Saudi Arabia\"},{\"Code\":\"SN\",\"Name\":\"Senegal\"},{\"Code\":\"RS\",\"Name\":\"Serbia\"},{\"Code\":\"SC\",\"Name\":\"Seychelles\"},{\"Code\":\"SL\",\"Name\":\"Sierra Leone\"},{\"Code\":\"SG\",\"Name\":\"Singapore\"},{\"Code\":\"SX\",\"Name\":\"Sint Maarten (Dutch part)\"},{\"Code\":\"SK\",\"Name\":\"Slovakia\"},{\"Code\":\"SI\",\"Name\":\"Slovenia\"},{\"Code\":\"SB\",\"Name\":\"Solomon Islands\"},{\"Code\":\"SO\",\"Name\":\"Somalia\"},{\"Code\":\"ZA\",\"Name\":\"South Africa\"},{\"Code\":\"GS\",\"Name\":\"South Georgia and the South Sandwich Islands\"},{\"Code\":\"SS\",\"Name\":\"South Sudan\"},{\"Code\":\"ES\",\"Name\":\"Spain\"},{\"Code\":\"LK\",\"Name\":\"Sri Lanka\"},{\"Code\":\"SD\",\"Name\":\"Sudan\"},{\"Code\":\"SR\",\"Name\":\"Suriname\"},{\"Code\":\"SJ\",\"Name\":\"Svalbard and Jan Mayen\"},{\"Code\":\"SZ\",\"Name\":\"Swaziland\"},{\"Code\":\"SE\",\"Name\":\"Sweden\"},{\"Code\":\"CH\",\"Name\":\"Switzerland\"},{\"Code\":\"SY\",\"Name\":\"Syrian Arab Republic\"},{\"Code\":\"TW\",\"Name\":\"Taiwan, Province of China\"},{\"Code\":\"TJ\",\"Name\":\"Tajikistan\"},{\"Code\":\"TZ\",\"Name\":\"Tanzania, United Republic of\"},{\"Code\":\"TH\",\"Name\":\"Thailand\"},{\"Code\":\"TL\",\"Name\":\"Timor-Leste\"},{\"Code\":\"TG\",\"Name\":\"Togo\"},{\"Code\":\"TK\",\"Name\":\"Tokelau\"},{\"Code\":\"TO\",\"Name\":\"Tonga\"},{\"Code\":\"TT\",\"Name\":\"Trinidad and Tobago\"},{\"Code\":\"TN\",\"Name\":\"Tunisia\"},{\"Code\":\"TR\",\"Name\":\"Turkey\"},{\"Code\":\"TM\",\"Name\":\"Turkmenistan\"},{\"Code\":\"TC\",\"Name\":\"Turks and Caicos Islands\"},{\"Code\":\"TV\",\"Name\":\"Tuvalu\"},{\"Code\":\"UG\",\"Name\":\"Uganda\"},{\"Code\":\"UA\",\"Name\":\"Ukraine\"},{\"Code\":\"AE\",\"Name\":\"United Arab Emirates\"},{\"Code\":\"GB\",\"Name\":\"United Kingdom\"},{\"Code\":\"US\",\"Name\":\"United States\"},{\"Code\":\"UM\",\"Name\":\"United States Minor Outlying Islands\"},{\"Code\":\"UY\",\"Name\":\"Uruguay\"},{\"Code\":\"UZ\",\"Name\":\"Uzbekistan\"},{\"Code\":\"VU\",\"Name\":\"Vanuatu\"},{\"Code\":\"VE\",\"Name\":\"Venezuela, Bolivarian Republic of\"},{\"Code\":\"VN\",\"Name\":\"Viet Nam\"},{\"Code\":\"VG\",\"Name\":\"Virgin Islands, British\"},{\"Code\":\"VI\",\"Name\":\"Virgin Islands, U.S.\"},{\"Code\":\"WF\",\"Name\":\"Wallis and Futuna\"},{\"Code\":\"EH\",\"Name\":\"Western Sahara\"},{\"Code\":\"YE\",\"Name\":\"Yemen\"},{\"Code\":\"ZM\",\"Name\":\"Zambia\"},{\"Code\":\"ZW\",\"Name\":\"Zimbabwe\"}]"
            //writeJSONtoFile(fileName)
            countrylist = Gson().fromJson(str, Array<TypeForCountry>::class.java).toMutableList()

            return countrylist
        }

        //SHARED PREFERENCE PARA LOGIN USUARIO
        fun getDataUser(context: Context): String {
            var id: String = ""

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            id = sharedPrefs.getString(Constant.PREF_USER_ID, "").toString()

            return id
        }

        fun getTypeUser(context: Context): String {
            var typeUser: String = ""

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            typeUser = sharedPrefs.getString(Constant.TYPE_USER, "").toString()

            return typeUser
        }

        fun getDataCompleteNomUser(context: Context): String {
            var firstName: String = ""
            var lastName: String = ""
            var completeNom: String = ""

            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            firstName = sharedPrefs.getString(Constant.PREF_USER_FIRSTNAME, "").toString()
            lastName = sharedPrefs.getString(Constant.PREF_USER_LASTNAME, "").toString()
            completeNom = firstName + " " + lastName
            return completeNom
        }

        fun setDataUser(
            context: Context,
            strid: String,
            strfirstname: String,
            strlastname: String,
            stremail: String,
            strtypeuser: String
        ) {
            val sharedPrefs = context.getSharedPreferences(
                Constant.PREF_NAME, Context.MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.putString(Constant.PREF_USER_ID, strid)
            editor.putString(Constant.PREF_USER_FIRSTNAME, strfirstname)
            editor.putString(Constant.PREF_USER_LASTNAME, strlastname)
            editor.putString(Constant.PREF_USER_EMAIL, stremail)
            editor.putString(Constant.TYPE_USER, strtypeuser)
            editor.commit()
        }

//        fun setNotification(Task t)

        fun createCustomToast(
            message: String,
            context: Context,
            layoutInflater: View,
            typeMessage: String
        ) {
            if (typeMessage == "success") {
                layoutInflater.setBackground(
                    context.getResources().getDrawable(R.drawable.toast_success)
                )
                layoutInflater.textViewToast.setTextColor(
                    context.getResources().getColor(R.color.colorSuccess)
                )
            }
            if (typeMessage == "warning") {
                layoutInflater.setBackground(
                    context.getResources().getDrawable(R.drawable.toast_warning)
                )
                layoutInflater.textViewToast.setTextColor(
                    context.getResources().getColor(R.color.colorWarning)
                )
            }
            if (typeMessage == "danger") {
                layoutInflater.setBackground(
                    context.getResources().getDrawable(R.drawable.toast_danger)
                )
                layoutInflater.textViewToast.setTextColor(
                    context.getResources().getColor(R.color.colorDanger)
                )
            }
            layoutInflater.textViewToast.text = message
            val myToast = Toast(context)
            myToast.duration = Toast.LENGTH_LONG
            myToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            myToast.view = layoutInflater//setting the view of custom toast layout
            myToast.show()
        }

        fun getVolleyError(error: VolleyError, activity: Activity): String {
            var errorMsg = ""
            if (error is NoConnectionError) {
                val cm =
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activeNetwork: NetworkInfo? = null
                activeNetwork = cm.activeNetworkInfo
                errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                    "Servidor no esta conectado a internet. Por favor vuelva a intentar"
                } else {
                    "Tu dispositivo no esta conectado a internet. Por favor pruebe nuevamente cuando active la conexión a internet"
                }
            } else if (error is NetworkError || error.cause is ConnectException) {
                errorMsg =
                    "Tu dispositivo no esta conectado a internet. Por favor pruebe nuevamente cuando active la conexion a internet"
            } else if (error.cause is MalformedURLException) {
                errorMsg = "Ha ocurrido algo mal en la solicitud, pruebe de nuevo…"
            } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
                errorMsg = "Humbo un error parseando la información …"
            } else if (error.cause is OutOfMemoryError) {
                errorMsg = "Dispositivo sin memoria"
            } else if (error is AuthFailureError) {
                errorMsg = "Error en la autenticación, Por favor verifique usuario y su contraseña"
            } else if (error is ServerError || error.cause is ServerError) {
                errorMsg =
                    "El correo que intenta registrar ya fue registrado, verifique con soporte o vuelva a intentar"
            } else if (error is TimeoutError || error.cause is SocketTimeoutException || error.cause is ConnectTimeoutException || error.cause is SocketException || (error.cause!!.message != null && error.cause!!.message!!.contains(
                    "Tu conexion se agoto, pruebe de nuevo"
                ))
            ) {
                errorMsg = "Se agotó el tiempo de conexión, Vuelva a intentarlo"
            } else {
                errorMsg =
                    "Se agotó el tiempo de conexión. Vuelva a intentarlo. Vuelva a intentarlo."
            }

            return errorMsg
        }

    }
}