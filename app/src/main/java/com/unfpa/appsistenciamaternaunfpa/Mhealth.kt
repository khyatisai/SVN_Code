package com.unfpa.appsistenciamaternaunfpa

import android.annotation.SuppressLint
import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import com.nostra13.universalimageloader.utils.StorageUtils
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import java.io.File


class Mhealth : Application() {

    companion object {
        lateinit var firebaseAnalytics: FirebaseAnalytics
        lateinit var database: DatabaseReference
        private val TAG = Mhealth::class.java.simpleName
        @get:Synchronized
        var appInstance: Mhealth? = null
            private set
        val imageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.image_placeholder) // resource or drawable
            .showImageForEmptyUri(R.drawable.image_placeholder) // resource or drawable
            .showImageOnFail(R.drawable.image_placeholder) // resource or drawable
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build()
    }

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()
//        CalligraphyConfig.initDefault(
//            CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        )


        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(resources.getString(R.font.roboto))
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )


        appInstance = this

        val cacheDir: File = StorageUtils.getCacheDirectory(this)

        val config = ImageLoaderConfiguration.Builder(this)
            .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
            .diskCacheExtraOptions(480, 800, null)
            .threadPoolSize(3) // default
            .threadPriority(Thread.NORM_PRIORITY - 1) // default
            .tasksProcessingOrder(QueueProcessingType.FIFO) // default
            .denyCacheImageMultipleSizesInMemory()
            .memoryCache(LruMemoryCache(2 * 1024 * 1024))
            .memoryCacheSize(2 * 1024 * 1024)
            .memoryCacheSizePercentage(13) // default
            .diskCache(UnlimitedDiskCache(cacheDir)) // default
            .diskCacheSize(50 * 1024 * 1024)
            .diskCacheFileCount(100)
            .diskCacheFileNameGenerator(HashCodeFileNameGenerator()) // default
            .imageDownloader(BaseImageDownloader(this)) // default
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
            .writeDebugLogs()
            .build()

        // Apply configuration
        ImageLoader.getInstance().init(config);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        database = FirebaseDatabase.getInstance().reference


    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                /*val cache = DiskBasedCache(applicationContext.getCacheDir(), 10 * 1024 * 1024)
                val network = BasicNetwork(HurlStack())
                var requestQueue = Volley.newRequestQueue(applicationContext)
                requestQueue.cache = cache*/
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, TAG: String) {
        request.tag = TAG
        requestQueue?.add(request)
    }


}