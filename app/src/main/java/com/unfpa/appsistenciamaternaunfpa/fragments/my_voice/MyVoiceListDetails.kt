package com.unfpa.appsistenciamaternaunfpa.fragments.my_voice


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.ImageView

import com.unfpa.appsistenciamaternaunfpa.R
import kotlinx.android.synthetic.main.fragment_voice_list_details.*

/**
 * A simple [Fragment] subclass.
 */
class MyVoiceListDetails : androidx.fragment.app.Fragment() {
    lateinit var title: String
    lateinit var story: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_voice_list_details, container, false)
        setHasOptionsMenu(true)
        try{
            title = HtmlCompat.fromHtml((this.arguments!!.getString("title")?.toString()!!), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            story = HtmlCompat.fromHtml((this.arguments!!.getString("story")?.toString()!!), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } catch (e:Exception){
            e.printStackTrace()
        }
        val imgVoiceShare = rootView.findViewById<ImageView>(R.id.imgVoiceShare)
        imgVoiceShare.setOnClickListener {
            shareContent()
        }
        return rootView
    }

    private fun shareContent(){
        try {
            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, activity!!.getString(R.string.information_on_my_story))//"Information on My Story"
                putExtra(
                    Intent.EXTRA_TEXT, activity!!.getString(R.string.information_on_where_you_can_find_imp_services) + "\n \n" +
                            activity!!.getString(R.string.story_name) + title + "," +"\n" +
                            activity!!.getString(R.string.story_desc) +story + "," +"\n")
                // (Optional) Here we're setting the title of the content
                putExtra(Intent.EXTRA_TITLE, activity!!.getString(R.string.sharing_my_story_details_as_below))
                type = "text/plain"
                // (Optional) Here we're passing a content URI to an image to be displayed
                //setClipData(contentUri);
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }, null)
            startActivity(share)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            (activity as AppCompatActivity).supportActionBar?.title = title
            txtVoiceTitle.text = title
            txtVoiceDetailedDesc.text = story
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu.findItem(R.id.notification).isVisible = false
            menu.findItem(R.id.home).isVisible = true
        }
    }
}
