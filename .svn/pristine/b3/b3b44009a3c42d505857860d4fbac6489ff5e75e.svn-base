package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.sundeepk.compactcalendarview.domain.Event
import com.unfpa.appsistenciamaternaunfpa.R
import kotlinx.android.synthetic.main.activity_period_list.*
import java.util.*
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.text.SimpleDateFormat


/**
 * Created by KhyatiShah on 10/23/2019.
 */
class PeriodListActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var listEvents: MutableList<Event> = mutableListOf<Event>()

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_list)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.PeriodsLogs)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })


        val periodTrackerDAO =
            MhealthRoomDatabase.getAppDataBase(this!!.applicationContext)!!.periodTrackerDAO()
        val lstPeriods = periodTrackerDAO.getAllPeriods()

        val cal = Calendar.getInstance()
        for (i in 0 until lstPeriods.size) {
            if (!TextUtils.isEmpty(lstPeriods.get(i).startDate) && !TextUtils.isEmpty(lstPeriods.get(i).endDate)) {
                val simpleDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                val dtStartDt = simpleDateFormat.parse(lstPeriods.get(i).startDate)
                val dtEndDt = simpleDateFormat.parse(lstPeriods.get(i).endDate)
                val start = Calendar.getInstance()
                start.time = dtStartDt
                val end = Calendar.getInstance()
                end.time = dtEndDt

                var date = start.time
                while (start.before(end)) {
                    // Do your job here with `date`.
                    //System.out.println(date)
                    /*listEvents.add(
                        Event(Color.RED, date.time)
                    )*/
                    compactcalendar_view.addEvent(Event(Color.RED, date.time), true)
                    start.add(
                        Calendar.DATE, 1
                    )
                    date = start.time
                }
                compactcalendar_view.addEvent(Event(Color.RED, dtEndDt.time), true)
            } else {
                if (!TextUtils.isEmpty(lstPeriods.get(i).startDate)) {
                    val simpleDateFormat = SimpleDateFormat(Constant.PERIOD_DATE_FORMAT)
                    val dtStartDt = simpleDateFormat.parse(lstPeriods.get(i).startDate)
                    /*listEvents.add(
                        Event(Color.RED, dtStartDt.time)
                    )*/
                    compactcalendar_view.addEvent(Event(Color.RED, dtStartDt.time), true)
                }
            }
        }

        /* // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
         val ev1 = Event(Color.RED, cal.timeInMillis, "Some extra data that I want to store.")
         cal.add(
             Calendar.DATE,
             1
         )
         val ev2 = Event(Color.RED, cal.timeInMillis)

         myList.add(ev1)
         myList.add(ev2)
        */
        //compactcalendar_view.addEvents(listEvents)

        val simpleDateFormat = SimpleDateFormat("MMM YYYY")
        txtMonthName.text = simpleDateFormat.format(compactcalendar_view.getFirstDayOfCurrentMonth())

        //set title on calendar scroll
        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {}

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                txtMonthName.text = simpleDateFormat.format(compactcalendar_view.getFirstDayOfCurrentMonth())
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        if (menu != null) {
            menu.findItem(R.id.home).isVisible = false
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
}