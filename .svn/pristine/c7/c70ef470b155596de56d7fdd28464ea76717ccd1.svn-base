package com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.*
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHContentImageAdapter
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHContentVideoListAdapter
import com.unfpa.appsistenciamaternaunfpa.api_controller.EndPoints
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import kotlinx.android.synthetic.main.fragment_srh_content_detail.*
import kotlin.Exception
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge.SRHContentTopicsAdapter
import com.unfpa.appsistenciamaternaunfpa.api_controller.ServiceVolley
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge.SRHContentDAO
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import com.nostra13.universalimageloader.core.ImageLoader
import com.unfpa.appsistenciamaternaunfpa.Mhealth
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_delete_reminder.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by KhyatiShah on 8/20/2019.
 */
class SRHContentDetailFragment : androidx.fragment.app.Fragment() {

    lateinit var contentId: String
    lateinit var srhContent: SRHContent
    lateinit var contentMasterDAO: SRHContentDAO
    val WRITE_STORAGE_PERMISSION_REQ_CODE = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_srh_content_detail, container, false)
        setHasOptionsMenu(true)
        contentId = this.arguments!!.getString("contentId").toString()
        //Logging firebase screen
        AppUtils.trackScreen(Constant.SRH_CONTENT_VIEW, this!!.activity!!)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            contentMasterDAO =
                MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.contentMasterDAO()
            srhContent = contentMasterDAO.getContentById(contentId)

            //setting actionbar title
            (activity as AppCompatActivity).supportActionBar?.title =
                HtmlCompat.fromHtml(srhContent.title.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            imgPageShare.setOnClickListener {
                //Logging firebase screen
                AppUtils.trackScreen(Constant.SRH_CONTENT_SHARE, this!!.activity!!)
                showImageSharePopup()
            }

            //setting cover image
            if (!TextUtils.isEmpty(srhContent.field_cover_image)) {
                coverImageView.visibility = View.VISIBLE
                val service = ServiceVolley()
                val strURL = (EndPoints.URL_ROOT + srhContent.field_cover_image).replace("\\s".toRegex(), "")
                //coverImageView.setImageUrl(strURL, service.imageLoader)
                val imageLoader = ImageLoader.getInstance()
                imageLoader.displayImage(strURL, coverImageView, Mhealth.imageOptions)
            } else {
                coverImageView.visibility = View.GONE
            }
            txtTitle.text =
                HtmlCompat.fromHtml(srhContent.title.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            if (!TextUtils.isEmpty(srhContent.field_short_description)) {
                llShortDescContainer.visibility = View.VISIBLE
                txtShortDesc.text =
                    HtmlCompat.fromHtml(srhContent.field_short_description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        .toString()
            } else {
                llShortDescContainer.visibility = View.GONE
            }

            if (srhContent.isFavourite) {
                imgFav.setImageDrawable(
                    ContextCompat.getDrawable(this.activity!!.applicationContext, R.drawable.ic_page_fav_active)
                )
            } else {
                imgFav.setImageDrawable(
                    ContextCompat.getDrawable(this.activity!!.applicationContext, R.drawable.ic_page_fav)
                )
            }
            imgFav.setOnClickListener {
                setFavourite()

            }
            // lstImages.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            linearLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
            lstImages!!.layoutManager = linearLayoutManager
            lstImages!!.isNestedScrollingEnabled = true
            lstImages!!.setHasFixedSize(true)

            val srhContentImageAdapter = SRHContentImageAdapter(this.activity!!)
            lstImages.adapter = srhContentImageAdapter
            if (srhContent.field_image != "") {
                srhContentImageAdapter.setIamgeList(srhContent.field_image!!.split(","))
            } else {
                llHeaderImageList.visibility = View.GONE
                lstImages.visibility = View.GONE
                srhContentImageAdapter.setIamgeList(emptyList())
            }

            val linearLayoutManagerVideo = androidx.recyclerview.widget.LinearLayoutManager(activity)
            linearLayoutManagerVideo.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
            lstVideos!!.layoutManager = linearLayoutManagerVideo
            lstVideos!!.isNestedScrollingEnabled = true
            lstVideos!!.setHasFixedSize(true)

            val srhContentVideoAdapter = SRHContentVideoListAdapter(this.activity!!)
            lstVideos.adapter = srhContentVideoAdapter
            if (srhContent.field_video != "")
                srhContentVideoAdapter.setIamgeList(srhContent.field_video!!.split(","))
            else {
                llHeaderVideoList.visibility = View.GONE
                lstVideos.visibility = View.GONE
                srhContentVideoAdapter.setIamgeList(emptyList())
            }

            //setting recycler view for topics
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(activity)
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setGravityResolver { Gravity.CENTER }
                .setRowBreaker { position -> position == 6 || position == 11 || position == 2 }
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .withLastRow(true)
                .build()
            lstTopics!!.layoutManager = chipsLayoutManager
            ViewCompat.setLayoutDirection(lstTopics, ViewCompat.LAYOUT_DIRECTION_LTR);
            val topicsAdapter = SRHContentTopicsAdapter(this.activity!!)
            lstTopics.adapter = topicsAdapter
            if (srhContent.field_tags != "")
                topicsAdapter.setTopicList(srhContent.field_tags!!.split(","))
            else {
                llHeaderTopicList.visibility = View.GONE
                lstTopics.visibility = View.GONE
                topicsAdapter.setTopicList(emptyList())
            }
            detailedDesc.setHtml(
                srhContent.field_description!!,
                HtmlHttpImageGetter(detailedDesc, EndPoints.URL_ROOT, true)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

    private fun shareContent() {
        try {
            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    HtmlCompat.fromHtml(
                        srhContent.field_sms_text.toString(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString()
                )
                putExtra(
                    Intent.EXTRA_TITLE,
                    HtmlCompat.fromHtml(srhContent.title.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                )

                putExtra(Intent.EXTRA_SUBJECT, "Important information for you");

                type = "text/plain"
                // (Optional) Here we're passing a content URI to an image to be displayed
                //setClipData(contentUri);
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }, null)
            startActivity(share)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun setFavourite() {
        if (srhContent.isFavourite) {
            srhContent.isFavourite = false
            imgFav.setImageDrawable(
                ContextCompat.getDrawable(this.activity!!.applicationContext, R.drawable.ic_page_fav)
            )
            contentMasterDAO.setFavourite(srhContent.nid, false)
        } else {
            //Logging firebase screen
            AppUtils.trackScreen(Constant.SRH_CONTENT_FAVORITE, this!!.activity!!)
            srhContent.isFavourite = true
            imgFav.setImageDrawable(
                ContextCompat.getDrawable(this.activity!!.applicationContext, R.drawable.ic_page_fav_active)
            )
            contentMasterDAO.setFavourite(srhContent.nid, true)
        }
    }

    fun showImageSharePopup() {
        if (!TextUtils.isEmpty(srhContent.field_cover_image)) {
            val dialog = activity?.let { Dialog(it) }
            if (dialog != null) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_delete_reminder)
                dialog.txtMessage.text = activity!!.getString(R.string.image_share_srh_content)
                dialog.txtHeader.text = activity!!.getString(R.string.Share_Content)
                dialog.txtNo.setOnClickListener {
                    dialog.dismiss()
                    shareContent()
                }
            }


            if (dialog != null) {
                dialog.txtYes.setOnClickListener {
                    dialog.dismiss()
                    checkForStoragePermission()
                }
            }
            if (dialog != null) {
                dialog.show()
            }
        } else {
            shareContent()
        }
    }

    fun shareImage() {
        val uri = getLocalBitmapUri(coverImageView)
        try {
            if (uri != null) {
                val share = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        HtmlCompat.fromHtml(
                            srhContent.field_sms_text.toString(),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString()
                    )
                    putExtra(
                        Intent.EXTRA_TITLE,
                        HtmlCompat.fromHtml(srhContent.title.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                    )
                    putExtra(Intent.EXTRA_STREAM, uri);
                    putExtra(Intent.EXTRA_SUBJECT, "Important information for you");

                    type = "image/*"
                    // (Optional) Here we're passing a content URI to an image to be displayed
                    //setClipData(contentUri);
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }, null)
                startActivity(share)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getLocalBitmapUri(imageView: ImageView): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.getDrawable()
        var bmp: Bitmap? = null
        if (drawable is BitmapDrawable) {
            bmp = (imageView.getDrawable() as BitmapDrawable).bitmap
        } else {
            return null
        }
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ), "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.getParentFile().mkdirs()
            val out = FileOutputStream(file)
            bmp!!.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            //bmpUri = Uri.fromFile(file)
            bmpUri = FileProvider.getUriForFile(
                this!!.activity!!,
                this.activity!!.packageName + ".provider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }

    fun checkForStoragePermission() {
        //Checking permission for write external storage to store offline images
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(
                    activity as AppCompatActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissionList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(
                    permissionList,
                    WRITE_STORAGE_PERMISSION_REQ_CODE
                )
            } else {
                //share image
                shareImage()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            WRITE_STORAGE_PERMISSION_REQ_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {
                    //share image
                    shareImage()
                }
            }
        }
    }
}