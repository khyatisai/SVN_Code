package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.toolbox.NetworkImageView
import com.unfpa.appsistenciamaternaunfpa.api_controller.ServiceVolley
import androidx.appcompat.app.AppCompatActivity



/**
 * A simple [Fragment] subclass.
 */
class ImageViewFragment : androidx.fragment.app.Fragment() {
    private var service = ServiceVolley()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.hide()

        val rootView = inflater.inflate(com.unfpa.appsistenciamaternaunfpa.R.layout.fragment_image_view, container, false)
        val imageView = rootView.findViewById<NetworkImageView>(com.unfpa.appsistenciamaternaunfpa.R.id.networkImageView)

        if(arguments != null && arguments!!.getString("url") != null){
            val url = arguments!!.getString("url")
            imageView.setImageUrl(url, service.imageLoader)
        }
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}
