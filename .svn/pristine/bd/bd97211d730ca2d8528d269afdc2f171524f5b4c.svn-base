package com.unfpa.appsistenciamaternaunfpa.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.nostra13.universalimageloader.core.ImageLoader
import com.unfpa.appsistenciamaternaunfpa.BuildConfig
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.profile.ProfileActivity
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.callService
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.fragments.HomeDoctorFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.MyService.MyServiceFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.about.AboutFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_favourites.MyFavouritesFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_health.MyHealthLandingFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.MyKnowledgeFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.MyVoiceFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat.ChatFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.notification.NotificationFragment
import com.unfpa.appsistenciamaternaunfpa.fragments.settings.SettingsFragment
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_doctor_main.*
import kotlinx.android.synthetic.main.custom_toast_layout.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainDoctorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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
        setContentView(R.layout.activity_doctor_main)
        try {
            stopService(Intent(this@MainDoctorActivity, callService::class.java))
            toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            context = this

            val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layoutDoc)
            navView = findViewById(R.id.nav_viewDoc)
            val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            val countryOfficeDAO =
                MhealthRoomDatabase.getAppDataBase(this.applicationContext)!!.countryOfficeDAO().getAllContent()
            //val hView = nav_view.getHeaderView(0)
            //val imageView: ImageView = hView.findViewById(R.id.imageView1)

            val headerView = nav_viewDoc.getHeaderView(0)
            val navUsername =
                headerView.findViewById<View>(R.id.nombreUser) as TextView
            val versionNumero =
                headerView.findViewById<View>(R.id.versionNo) as TextView
            val getMyUserId = AppUtils.getDataCompleteNomUser(this)
            navUsername.text = "Dr/Dra. "+AppUtils.getDataCompleteNomUser(this)

            versionNumero.text = "Version No: "+BuildConfig.VERSION_NAME

            if (countryOfficeDAO.isNotEmpty()) {
                val sharedPreference = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
                val CountryCode = sharedPreference.getString(Constant.ITEM_COUNTRY_CODE, "")
                if (!CountryCode.isNullOrEmpty()) {
                    val countryOfficeDAO_1 = MhealthRoomDatabase.getAppDataBase(applicationContext)!!.countryOfficeDAO()
                    val countryCodeRequest = countryOfficeDAO_1.getModuleVisibility(CountryCode)
                    if (countryCodeRequest != null) {
                        val fieldImage: String = countryCodeRequest.field_image
                        if (fieldImage.isNotEmpty()) {
                            val imageLoader = ImageLoader.getInstance()
                            val strURL = (EndPoints.URL_ROOT + fieldImage).replace("\\s".toRegex(), "")
                            //imageLoader.displayImage(strURL, imageView, Mhealth.imageOptions)
                        }
                    }
                }
            }
            /* toolbar!!.setNavigationOnClickListener {
             onBackPressed()
         }*/


            /*navView!!.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }*/
            toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.colorBlue)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            /*toggle.setToolbarNavigationClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }*/

            this.navView!!.setNavigationItemSelectedListener(this)
            //adding home fragment as default
            toolbar!!.title = getString(R.string.app_name)
            toolbar!!.setBackgroundColor(getResources().getColor(R.color.colorDoctorBlue));
            toolbar!!.setTitleTextColor(getResources().getColor(R.color.white))
            toolbar!!.setNavigationIcon(R.drawable.ic_title_menu_whitee)

            setMenuChecked(0)
            //navView!!.setCheckedItem(0)
            AppUtils.addFragment(this, HomeDoctorFragment(), false, "")

            supportFragmentManager.addOnBackStackChangedListener(object :
                androidx.fragment.app.FragmentManager.OnBackStackChangedListener {
                override
                fun onBackStackChanged() {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        toggle.setDrawerIndicatorEnabled(false)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        try {
            val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layoutDoc)
            val nav_my_health = navView!!.menu.findItem(R.id.nav_my_health)
            val nav_my_services = navView!!.menu.findItem(R.id.nav_my_services)
            val nav_my_voice = navView!!.menu.findItem(R.id.nav_my_voice)
            val sharedPreference1 = this.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
            val CountryCode = sharedPreference1.getString(Constant.ITEM_COUNTRY_CODE, "")
            var field_enable_health_manager = "On"
            var field_enable_service_locator = "On"
            if (!CountryCode.isNullOrEmpty()) {
                val countryOfficeDAO = MhealthRoomDatabase.getAppDataBase(this.applicationContext)!!.countryOfficeDAO()
                val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode)
                field_enable_health_manager = countryCodeRequest.field_enable_health_manager
                field_enable_service_locator = countryCodeRequest.field_enable_service_locator
            }


            navView!!.setOnClickListener {
                //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }


    override fun onBackPressed() {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layoutDoc)
        val fragment: androidx.fragment.app.Fragment? =
            this.supportFragmentManager.findFragmentById(R.id.frame_content);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (fragment is ChatFragment) {
            (fragment as ChatFragment).onBeackPressed()
        } else if (fragment is AboutFragment || fragment is MyKnowledgeFragment || fragment is MyServiceFragment || fragment is MyHealthLandingFragment || fragment is MyVoiceFragment || fragment is SettingsFragment || fragment is NotificationFragment) {
            AppUtils.clearBackstack(this)
            AppUtils.addFragment(this, HomeDoctorFragment(), false, "")
        } else if (fragment is HomeDoctorFragment) {
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
    }

    private fun exitApp() {
        if (isFinish) {
            //finish()
            finishAffinity()
        } else {
            isFinish = true
            //Toast.makeText(this, R.string.main_exit, Toast.LENGTH_SHORT).show()
            val layoutToast = layoutInflater.inflate(R.layout.custom_toast_layout,custom_toast_container)
            applicationContext?.let { it1 ->
                AppUtils.createCustomToast("Haga clic en ATRÁS nuevamente para salir.", it1, layoutToast,"warning")
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menu.findItem(R.id.home).isVisible = false
        menu.findItem(R.id.add).isVisible = false
        menu.findItem(R.id.calendar).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                toolbar!!.title = getString(R.string.app_name)
                setMenuChecked(0)
                AppUtils.clearBackstack(this)
                AppUtils.addFragment(this, HomeDoctorFragment(), false, "")
                return true
            }
            R.id.notification -> {
                //Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                AppUtils.addFragment(this, NotificationFragment(), false, "")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setMenuChecked(index: Int) {
        onNavigationItemSelected(navView!!.menu.getItem(index))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        //item.isChecked = true

        when (item.itemId) {
            R.id.nav_home -> {
                navView!!.setCheckedItem(item)
                toolbar!!.title = getString(R.string.app_name)
                AppUtils.addFragment(this, HomeDoctorFragment(), false, "")
            }
            R.id.nav_my_knowledge -> {
                navView!!.setCheckedItem(item)
                toolbar!!.title = getString(R.string.menu_my_knowledge)
                AppUtils.addFragment(this, MyKnowledgeFragment(), false, "")
            }
            R.id.nav_my_services -> {
                navView!!.setCheckedItem(item)
                toolbar!!.title = getString(R.string.menu_my_services)
                AppUtils.addFragment(this, MyServiceFragment.newInstance(), false, "")
            }
            R.id.nav_my_health -> {
                navView!!.setCheckedItem(item)
                toolbar!!.title = getString(R.string.menu_my_health)
                AppUtils.addFragment(this, MyHealthLandingFragment(), false, "")
                //Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my_voice -> {
                toolbar!!.title = getString(R.string.menu_my_voice)
                navView!!.setCheckedItem(item)
                AppUtils.addFragment(this, MyVoiceFragment.newInstance(), false, "")

            }

            R.id.nav_call -> {
                toolbar!!.title = getString(R.string.menu_call)
                navView!!.setCheckedItem(item)
                AppUtils.addFragment(this, MyVoiceFragment.newInstance(), false, "")

            }

            R.id.nav_my_favourites -> {
                toolbar!!.title = getString(R.string.menu_my_favourites)
                navView!!.setCheckedItem(item)
                AppUtils.addFragment(this, MyFavouritesFragment(), false, "")
            }
            R.id.nav_about -> {
                toolbar!!.title = getString(R.string.menu_about)
                navView!!.setCheckedItem(item)
                AppUtils.addFragment(this, AboutFragment(), false, "")
            }
            R.id.nav_logout -> {
                try {
                    val sharedPreference1 = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
                    val UserId = sharedPreference1.getString(Constant.PREF_USER_ID, "")
                    val UserFirstName = sharedPreference1.getString(Constant.PREF_USER_FIRSTNAME, "")
                    val UserLastName = sharedPreference1.getString(Constant.PREF_USER_LASTNAME, "")
                    val UserEmail = sharedPreference1.getString(Constant.PREF_USER_EMAIL, "")
                    val TypeUser = sharedPreference1.getString(Constant.TYPE_USER, "")

                    var editor = sharedPreference1.edit()
                    editor.clear()
                    editor.apply()

//                    sharedPreference1.edit().remove(Constant.PREF_USER_ID).commit()
//                    sharedPreference1.edit().remove(Constant.PREF_USER_FIRSTNAME).commit()
//                    sharedPreference1.edit().remove(Constant.PREF_USER_LASTNAME).commit()
//                    sharedPreference1.edit().remove(Constant.PREF_USER_EMAIL).commit()
//                    sharedPreference1.edit().remove(Constant.TYPE_USER).commit()

                    finishAffinity()
                    val i = Intent(applicationContext, SplashActivity::class.java)        // Specify any activity here e.g. home or splash or login etc
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.putExtra("EXIT", true)
                    startActivity(i)
                    //finish()


//                    finishAffinity()
//
//                    var intent = Intent(this, SplashActivity::class.java)
//                    startActivity(intent)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.nav_help -> {
                /*toolbar!!.title = getString(R.string.menu_help)
                navView!!.setCheckedItem(item)
                AppUtils.addFragment(this, HelpFragment(), false, "")*/
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                toolbar!!.title = getString(R.string.menu_settings)
                navView!!.setCheckedItem(item)
                AppUtils.addFragment(this, SettingsFragment(), false, "")
                //navView!!.setCheckedItem(item)
                //Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_profile -> {
                //toolbar!!.title = getString(R.string.menu_profile)
                //navView!!.setCheckedItem(item)
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layoutDoc)
        drawerLayout.closeDrawer(GravityCompat.START)
        invalidateOptionsMenu()
        return true
    }


}
