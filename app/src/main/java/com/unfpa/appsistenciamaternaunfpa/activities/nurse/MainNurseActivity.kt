package com.unfpa.appsistenciamaternaunfpa.activities.nurse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.unfpa.appsistenciamaternaunfpa.BuildConfig
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.SplashActivity
import com.unfpa.appsistenciamaternaunfpa.callService
import com.unfpa.appsistenciamaternaunfpa.fragments.HomeDoctorFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.MyService.MyServiceFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.about.AboutFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_health.MyHealthLandingFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.MyKnowledgeFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.MyVoiceFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat.ChatFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.notification.NotificationFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.nurse.NurseHomeFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.settings.SettingsFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main_nurse.*
import kotlinx.android.synthetic.main.custom_toast_layout.*

class MainNurseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var toolbar: Toolbar? = null
    var context: Context? = null
    var isFinish: Boolean = false
    var navView: NavigationView? = null
    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nurse)
        try {
            stopService(Intent(this@MainNurseActivity, callService::class.java))
            toolbar = findViewById(R.id.toolbar)
            toolbar!!.title = ""
            setSupportActionBar(toolbar)
            context = this

            val drawerLayout: androidx.drawerlayout.widget.DrawerLayout =
                findViewById(R.id.drawer_layoutNurse)
            navView = findViewById(R.id.nav_viewNurse)
            val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            val headerView = nav_viewNurse.getHeaderView(0)
            val navUsername =
                headerView.findViewById<View>(R.id.nombreUser) as TextView
            val versionNumero =
                headerView.findViewById<View>(R.id.versionNo) as TextView
            navUsername.text = AppUtils.getDataCompleteNomUser(this)
            versionNumero.text = "Version No: " + BuildConfig.VERSION_NAME

            toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.colorBlack)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            this.navView!!.setNavigationItemSelectedListener(this)
            //adding home fragment as default

            toolbar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.nurse_green_color));
            toolbar!!.setTitleTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            toolbar!!.setNavigationIcon(R.drawable.ic_title_menu_black)
            var title = this.getString(R.string.app_name)
            toolbar!!.title = "Appsistencia Materna"

            setMenuChecked(0)
            //AppUtils.addFragment(this, NurseHomeFragment(), false, "")
            supportFragmentManager.addOnBackStackChangedListener(object :
                androidx.fragment.app.FragmentManager.OnBackStackChangedListener {
                override
                fun onBackStackChanged() {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        toggle.setDrawerIndicatorEnabled(false)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
                        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
                            override
                            fun onClick(v: View) {
                                onBackPressed()
                            }
                        })
                    } else {
                        //show hamburger
                        toggle.setDrawerIndicatorEnabled(true)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                        toggle.syncState()
                        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
                            override
                            fun onClick(v: View) {
                                drawerLayout.openDrawer(GravityCompat.START)
                            }
                        })
                    }
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                navView!!.setCheckedItem(item)
                toolbar!!.title = getString(R.string.app_name)
                AppUtils.addFragment(this, NurseHomeFragment(), false, "")
            }
            R.id.nav_about -> {
                try {
                    toolbar!!.title = getString(R.string.menu_about)
                    navView!!.setCheckedItem(item)
                    AppUtils.addFragment(this, AboutFragment(), false, "")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.nav_logout -> {
                try {
                    val sharedPreference1 =
                        getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
                    var editor = sharedPreference1.edit()
                    editor.clear()
                    editor.apply()

                    finishAffinity()
                    val i = Intent(
                        applicationContext,
                        SplashActivity::class.java
                    )        // Specify any activity here e.g. home or splash or login etc
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.putExtra("EXIT", true)
                    startActivity(i)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout =
            findViewById(R.id.drawer_layoutNurse)
        drawerLayout.closeDrawer(GravityCompat.START)
        invalidateOptionsMenu()
        return true
    }

    fun setMenuChecked(index: Int) {
        onNavigationItemSelected(navView!!.menu.getItem(index))
    }

    override fun onBackPressed() {
        try {
            val drawerLayout: androidx.drawerlayout.widget.DrawerLayout =
                findViewById(R.id.drawer_layoutNurse)
            val fragment: androidx.fragment.app.Fragment? =
                this.supportFragmentManager.findFragmentById(R.id.frame_content);

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else if (fragment is ChatFragment) {
                (fragment as ChatFragment).onBeackPressed()
            } else if (fragment is AboutFragment || fragment is MyKnowledgeFragment || fragment is MyServiceFragment || fragment is MyHealthLandingFragment || fragment is MyVoiceFragment || fragment is SettingsFragment || fragment is NotificationFragment) {
                AppUtils.clearBackstack(this)
                AppUtils.addFragment(this, NurseHomeFragment(), false, "")
            } else if (fragment is NurseHomeFragment) {
//            if (llSearchResult.visibility == View.VISIBLE) {
//                llSearchResult.visibility = View.GONE
//                llDashboard.visibility = View.VISIBLE
//                edtSearch.text.clear()
//            } else {
//                exitApp()
//            }
                exitApp()
            } else {
                /*val sharedPreference =  this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            *//*editor.putBoolean("flag", true)*//*
            editor.remove("flag")
            editor.apply()
            editor.commit()
            editor.clear()*/
                super.onBackPressed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                toolbar!!.title = getString(R.string.app_name)
                setMenuChecked(0)
                AppUtils.clearBackstack(this)
                AppUtils.addFragment(this, NurseHomeFragment(), false, "")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menu.findItem(R.id.home).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        menu.findItem(R.id.calendar).isVisible = false
        menu.findItem(R.id.calendar).isVisible = false
        menu.findItem(R.id.notification).isVisible = false
        return true
    }

    private fun exitApp() {
        if (isFinish) {
            //finish()
            finishAffinity()
        } else {
            isFinish = true
            //Toast.makeText(this, R.string.main_exit, Toast.LENGTH_SHORT).show()
            val layoutToast =
                layoutInflater.inflate(R.layout.custom_toast_layout, custom_toast_container)
            applicationContext?.let { it1 ->
                AppUtils.createCustomToast(
                    "Haga clic en ATRÁS nuevamente para salir.",
                    it1,
                    layoutToast,
                    "warning"
                )
            }
            val thread = Thread(Runnable {
                try {
                    Thread.sleep(200000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                isFinish = false
            })
            thread.start()
        }
    }

}