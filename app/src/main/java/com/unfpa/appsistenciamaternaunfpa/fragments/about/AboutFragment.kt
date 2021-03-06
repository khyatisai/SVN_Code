package com.unfpa.appsistenciamaternaunfpa.fragments.about

import android.os.Bundle
import android.view.*
import com.unfpa.appsistenciamaternaunfpa.R

/**
 * Created by KhyatiShah on 2/7/2020.
 */
class AboutFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        txtWelcomeDesc2.text =
//            HtmlCompat.fromHtml(getString(R.string.detalle), HtmlCompat.FROM_HTML_MODE_LEGACY)
//                .toString()
        val rootView = inflater.inflate(R.layout.fragment_about, container, false)
        setHasOptionsMenu(true)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu?.findItem(R.id.notification)?.isVisible = false
            menu?.findItem(R.id.home)?.isVisible = true
        }
    }
}