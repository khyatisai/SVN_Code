package com.unfpa.appsistenciamaternaunfpa.adapters.introductory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_item_content.view.*

class IntroCountryAdapter(
    private val IntroList: ArrayList<TypeForCountry>,
    activity: androidx.fragment.app.FragmentActivity,
    itemName: String,
    itemCode: String
) : androidx.recyclerview.widget.RecyclerView.Adapter<IntroCountryAdapter.CountryViewHolder>() {
    private var lastChecked: CheckBox? = null
    private var lastCheckedPos = 0
    private var activity: androidx.fragment.app.FragmentActivity = activity
    private var itemName: String = itemName
    private var itemCode: String = itemCode
    //private lateinit var context: Context

    var selected: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_slide_item_content, parent, false)
        return CountryViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        lastCheckedPos = position
        holder.textViewName.text = IntroList[position].name
        holder.checkBox.isChecked = IntroList[position].isChecked

        holder.itemView.cardView.setOnClickListener {
            holder.checkBox.isChecked = true

            for (i in 0 until IntroList.size) {
                IntroList[i].isChecked = false
                //holder.textViewName.setTextColor(Color.parseColor("#000000"))
            }
            IntroList[position].isChecked = true
    //holder.textViewName.setTextColor(Color.parseColor("#0062A9"))
    val sharedPreference = activity.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
    var editor = sharedPreference.edit()
    editor.putString(itemName, IntroList[position].name)
    editor.putString(itemCode, IntroList[position].code)
    editor.apply()
    editor.commit()

    notifyDataSetChanged()
}
}


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return IntroList.size
    }

    class CountryViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.txtSlideName) as TextView
        val checkBox = itemView.findViewById(R.id.cbSelectItem) as CheckBox
        val radioGroupOptions = itemView.findViewById(R.id.radioGroupOptionsIntro) as RadioGroup
        /*fun bindItems(item: Type) {
        }*/
    }
}
