package com.unfpa.appsistenciamaternaunfpa.fragments.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.unfpa.appsistenciamaternaunfpa.R
import kotlinx.android.synthetic.main.fragment_about.*


/**
 * Created by KhyatiShah on 2/7/2020.
 */
class HelpFragment: androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //txtWelcomeDesc2.text = Html.fromHtml(getString(R.string.detalle))

        txtWelcomeDesc2.text =
            HtmlCompat.fromHtml(getString(R.string.detalle), HtmlCompat.FROM_HTML_MODE_LEGACY)
                .toString()
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}