package com.unfpa.appsistenciamaternaunfpa

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.android.synthetic.main.activity_beforecall.*

class beforecall : AppCompatActivity() {
    private val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
    private val requestcode = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beforecall)

        if (!isPermissionGranted()) {
            askPermissions()
        }

        Firebase.initialize(this)

       loginBtn.setOnClickListener{
           val username = usernameEdit.text.toString()
           val intent = Intent(this, call::class.java)
           intent.putExtra("username", username)
           startActivity(intent)
       }
    }

    private fun askPermissions() {
      ActivityCompat.requestPermissions(this, permissions, requestcode)
    }

    private fun isPermissionGranted(): Boolean {
    permissions.forEach {
        if(ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
            return false;
    }
        return true
    }
}