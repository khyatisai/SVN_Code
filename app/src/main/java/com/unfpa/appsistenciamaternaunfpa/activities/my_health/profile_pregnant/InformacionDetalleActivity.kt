package com.unfpa.appsistenciamaternaunfpa.activities.my_health.profile_pregnant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.patient.CustomAdapterConsejoCategoriaDetalleList
import com.unfpa.appsistenciamaternaunfpa.adapters.patient.CustomExpandableListAdapter
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class InformacionDetalleActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    var idInfoList: ArrayList<String> = ArrayList()
    var NameInfoList: ArrayList<String> = ArrayList()
    var DescriptionInfoList: ArrayList<String> = ArrayList()
    var ImageInfoList: ArrayList<String> = ArrayList()

    var getInfoDetail = EndPoints.URL_HEROKU + "/api/v1/tips/detail"

    internal var expandableListView: ExpandableListView? = null
    internal var adapterExpandable: ExpandableListAdapter? = null
    internal var titleList: List<String> ? = null

    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()

            val redmiMobiles = ArrayList<String>()
            redmiMobiles.add("Redmi Y2")
            redmiMobiles.add("Redmi S2")
            redmiMobiles.add("Redmi Note 5 Pro")
            redmiMobiles.add("Redmi Note 5")
            redmiMobiles.add("Redmi 5 Plus")
            redmiMobiles.add("Redmi Y1")
            redmiMobiles.add("Redmi 3S Plus")

            val micromaxMobiles = ArrayList<String>()
            micromaxMobiles.add("Micromax Bharat Go")
            micromaxMobiles.add("Micromax Bharat 5 Pro")
            micromaxMobiles.add("Micromax Bharat 5")
            micromaxMobiles.add("Micromax Canvas 1")
            micromaxMobiles.add("Micromax Dual 5")

            val appleMobiles = ArrayList<String>()
            appleMobiles.add("iPhone 8")
            appleMobiles.add("iPhone 8 Plus")
            appleMobiles.add("iPhone X")
            appleMobiles.add("iPhone 7 Plus")
            appleMobiles.add("iPhone 7")
            appleMobiles.add("iPhone 6 Plus")

            val samsungMobiles = ArrayList<String>()
            samsungMobiles.add("Samsung Galaxy S9+")
            samsungMobiles.add("Samsung Galaxy Note 7")
            samsungMobiles.add("Samsung Galaxy Note 5 Dual")
            samsungMobiles.add("Samsung Galaxy S8")
            samsungMobiles.add("Samsung Galaxy A8")
            samsungMobiles.add("Samsung Galaxy Note 4")

            listData["Redmi"] = redmiMobiles
            listData["Micromax"] = micromaxMobiles
            listData["Apple"] = appleMobiles
            listData["Samsung"] = samsungMobiles

            return listData
        }

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_detalle_paciente)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Información para usted"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back)
        toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
            override
            fun onClick(v: View) {
                finish()
            }
        })

        expandableListView = findViewById(R.id.expandableListView)

        expandableListView = findViewById(R.id.expandableListView)
        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapterExpandable = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapterExpandable)

            expandableListView!!.setOnGroupExpandListener {
                    groupPosition -> Toast.makeText(applicationContext, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show()
            }

            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
                Toast.makeText(applicationContext, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show()
            }

            expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Toast.makeText(applicationContext, "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }
        }


        getAllInformacionCategoria2()

//        btnAgregarPaciente.setOnClickListener{
//            var intent = Intent(this, PacienteAdd1Activity::class.java)
//            startActivity(intent)
//        }
    }

    private fun getAllInformacionCategoria2() {


        val jsonobj = JSONObject()
        jsonobj.put("categoryid", "1")

        val que = Volley.newRequestQueue(this)
        val req = JsonObjectRequest(
            Request.Method.POST, getInfoDetail, jsonobj,
            Response.Listener { response ->

                try {
                    val jsonArray = response.getJSONArray("tips")
                    for (i in 0 until jsonArray.length()) {
                        val ItemInfo = jsonArray.getJSONObject(i)
                        val idInfo = ItemInfo.getString("id")
                        val NameInfo = ItemInfo.getString("title")
                        val DescriptionInfo = ItemInfo.getString("description")
                        val ImageInfo = ItemInfo.getString("url_img")

                        idInfoList.add(idInfo)
                        NameInfoList.add(NameInfo)
                        DescriptionInfoList.add(DescriptionInfo)
                        ImageInfoList.add(ImageInfo)
                    }
                    val customAdapter = CustomAdapterConsejoCategoriaDetalleList(applicationContext, idInfoList, NameInfoList, DescriptionInfoList, ImageInfoList)
                    //recyclerMiConsejo.adapter = customAdapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error -> error.printStackTrace() })
        que?.add(req)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        if (menu != null) {
            menu.findItem(R.id.home).isVisible = false
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.calendar).isVisible = false
            menu.findItem(R.id.add).isVisible = false
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
            R.id.add -> {
                //var intent = Intent(this, PacienteAdd1Activity::class.java)
                startActivity(intent)
                //finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}