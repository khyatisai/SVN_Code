package com.unfpa.appsistenciamaternaunfpa.activities.my_health

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_period_tracker_input.*
import java.util.*

/**
 * Created by KhyatiShah on 10/11/2019.
 */
class PeriodTrackerInputActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_tracker_input)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.PeriodTracker)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);
        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            numberOfDays = newVal
        }
        //seeting min and max dates for period tracker
        var c = Calendar.getInstance()
        selectedDt = DateFormat.format(Constant.PERIOD_DATE_FORMAT, c).toString()
        //c.add(Calendar.MONTH, -1)
        //c.add(Calendar.DAY_OF_WEEK, -28)
        c.add(Calendar.DAY_OF_WEEK, -45)
        calendarView.minDate = c.timeInMillis
        c.add(Calendar.MONTH, 2)
        calendarView.maxDate = c.timeInMillis

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            selectedDt = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
        }
        btnDone.setOnClickListener {
            if (TextUtils.isEmpty(selectedDt)) {
                Toast.makeText(this, getString(R.string.EnterStartDate), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            /*if (numberOfDays == 0) {
                Toast.makeText(this, getString(R.string.EnterDays), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }*/
            val intent = Intent()
            intent.putExtra(Constant.NO_OF_DAYS, numberOfDays)
            intent.putExtra(Constant.SELECTED_DATE, selectedDt)
            setResult(1, intent);
            finish()
        }
        btnNext.setOnClickListener {
            rlDatePicker.visibility = View.GONE
            rlDaysPicker.visibility = View.VISIBLE
        }
        imgArrow.setOnClickListener {
            rlDatePicker.visibility = View.VISIBLE
            rlDaysPicker.visibility = View.GONE
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