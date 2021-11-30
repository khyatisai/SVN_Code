package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.webkit.WebChromeClient
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import com.unfpa.appsistenciamaternaunfpa.R


/**
 *
 */
//@Suppress("DEPRECATION")
class VideoViewFragment : androidx.fragment.app.Fragment() {
    //@SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.hide()
        val rootView = inflater.inflate(R.layout.fragment_video_view, container, false)

        val mWebView = rootView.findViewById<WebView>(R.id.videoView)
        /*mWebView.settings.javaScriptEnabled = true
        mWebView.settings.pluginState = WebSettings.PluginState.ON*/
        mWebView.getSettings().setJavaScriptEnabled(true)
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON)
        //mWebView.webViewClient = Browser()
        //mWebView.webChromeClient = MyWebClient()

        mWebView.webViewClient = WebViewClient()
        mWebView.webChromeClient = WebChromeClient()

//        if(arguments != null && arguments!!.getString("url") != null){
//            val url = arguments!!.getString("url")
//
//            val newUrl = "http://www.youtube.com/embed/"+ url!!.substring(url!!.indexOf("=")+1)+"?autoplay=1"
//
//            mWebView.loadUrl(newUrl)
//        }
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    internal inner class Browser : WebViewClient() {

        override fun shouldOverrideUrlLoading(paramWebView: WebView, paramString: String): Boolean {
            paramWebView.loadUrl(paramString)
            return true
        }
    }


    inner class MyWebClient : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: WebChromeClient.CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private var mOriginalOrientation: Int = 0
        private var mOriginalSystemUiVisibility: Int = 0

        override fun getDefaultVideoPoster(): Bitmap? {
            return if (activity!! == null) {
                null
            } else BitmapFactory.decodeResource(
                activity!!.getApplicationContext().getResources(),
                2130837573
            )
        }

        override fun onHideCustomView() {
            (activity!!.getWindow().getDecorView() as FrameLayout).removeView(this.mCustomView)
            this.mCustomView = null
            activity!!.getWindow().getDecorView()
                .setSystemUiVisibility(this.mOriginalSystemUiVisibility)
            activity!!.setRequestedOrientation(this.mOriginalOrientation)
            this.mCustomViewCallback!!.onCustomViewHidden()
            this.mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: WebChromeClient.CustomViewCallback
        ) {
            if (this.mCustomView != null) {
                onHideCustomView()
                return
            }
            this.mCustomView = paramView
            this.mOriginalSystemUiVisibility =
                activity!!.getWindow().getDecorView().getSystemUiVisibility()
            this.mOriginalOrientation = activity!!.getRequestedOrientation()
            this.mCustomViewCallback = paramCustomViewCallback
            (activity!!.getWindow().getDecorView() as FrameLayout).addView(
                this.mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )
            activity!!.getWindow().getDecorView().setSystemUiVisibility(3846)
        }
    }
}
