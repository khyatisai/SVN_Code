package com.unfpa.appsistenciamaternaunfpa.activities.my_health.profile_pregnant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.patient.CustomAdapterConsejoCategoriaList
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_informacion_paciente.*
import org.json.JSONException
import java.util.*


class InformacionActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var selectedDt: String = ""
    private var numberOfDays: Int = 1

    var IdConsejos: ArrayList<String> = ArrayList()
    var tvCategoriaConsejos: ArrayList<String> = ArrayList()



    var getAllCategoryInfo = EndPoints.URL_HEROKU + "/api/v1/category-tips/all"

    override fun attachBaseContext(newBase: Context) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_paciente)
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

        getAllInformacionCategoria2()

//        btnAgregarPaciente.setOnClickListener{
//            var intent = Intent(this, PacienteAdd1Activity::class.java)
//            startActivity(intent)
//        }
    }

    private fun getAllInformacionCategoria2() {

        recyclerMiConsejo.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, getAllCategoryInfo, null, Response.Listener {
                response ->try {
            val jsonArray = response.getJSONArray("categories")
            for (i in 0 until jsonArray.length()) {
                val categoria = jsonArray.getJSONObject(i)
                val id = categoria.getString("id")
                val Name = categoria.getString("name")

                IdConsejos.add(id)
                tvCategoriaConsejos.add(Name)
            }
            val customAdapter = CustomAdapterConsejoCategoriaList(applicationContext, IdConsejos, tvCategoriaConsejos)
            recyclerMiConsejo.adapter = customAdapter
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, Response.ErrorListener { error -> error.printStackTrace() })
        requestQueue?.add(request)
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