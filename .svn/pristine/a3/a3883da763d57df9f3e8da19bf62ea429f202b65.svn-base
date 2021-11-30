package com.unfpa.appsistenciamaternaunfpa.adapters.profile

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.Type
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import kotlinx.android.synthetic.main.fragment_slide_item_content_square.view.*

class IntroAdapterForInterest(
    private val IntroList: ArrayList<Type>,
    activity: androidx.fragment.app.FragmentActivity,
    itemName: String
) : androidx.recyclerview.widget.RecyclerView.Adapter<IntroAdapterForInterest.CountryViewHolder>() {
    private var activity: androidx.fragment.app.FragmentActivity = activity
    private var itemName: String = itemName
    //private lateinit var context: Context

    var selected: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_slide_item_content_square, parent, false)
        return CountryViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        //lastCheckedPos = position
        val sharedPreference = activity.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
        var strInterest = sharedPreference.getString(itemName, "")
        val lstValues: List<String> = strInterest?.split(",")!!.map { it -> it.trim() }
        lstValues.forEach { it ->
            Log.i("Values", "value=$it")
            if (it.trim().equals(IntroList[position].itemName.trim()))
                IntroList[position].isChecked = true
        }
        holder.textViewName.text = IntroList[position].itemName
        holder.checkBox.isChecked = IntroList[position].isChecked
        holder.checkBox.isClickable = false
        holder.checkBox.tag = position

        holder.itemView.cardViewSquare.setOnClickListener {

            var editor = sharedPreference.edit()
            var strInterest = sharedPreference.getString(itemName, "")
            val pos = holder.checkBox.tag as Int
            if (IntroList[pos].isChecked) {
                IntroList[pos].isChecked = false
                var newVal = ""
                val lstValues: List<String> = strInterest?.split(",")!!.map { it -> it.trim() }
                lstValues.forEach { it ->
                    Log.i("Values", "value=$it")
                    if (!it.trim().equals(IntroList[position].itemName.trim())) {
                        if (TextUtils.isEmpty(newVal)) {
                            newVal = it
                        } else {
                            newVal = newVal + "," + it
                        }
                    }
                    strInterest = newVal
                }
            } else {

                if (!TextUtils.isEmpty(strInterest)) {
                    strInterest = strInterest + "," + IntroList[position].itemName
                } else {
                    strInterest = IntroList[position].itemName
                }
                IntroList[pos].isChecked = true

            }
            editor.putString(itemName, strInterest)
            editor.apply()
            editor.commit()
            notifyDataSetChanged()
        }
        /*holder.checkBox.setOnClickListener {
            //holder.checkBox.isChecked =  true
            *//*for (i in 0 until IntroList.size)
            {
                val abc:Int = getItemId(i).toInt()
                holder.checkBox.isChecked = i == abc
            }*//*
            //IntroList[position].isChecked = true


            val sharedPreference =  activity.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString(itemName, IntroList[position].itemName)
            editor.apply()
            editor.commit()
            notifyDataSetChanged()
        }*/
    }


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return IntroList.size
    }

    class CountryViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.txtSlideNameSqaure) as TextView
        val checkBox = itemView.findViewById(R.id.cbSelectItemSquare) as CheckBox
        //val radioGroupOptions = itemView.findViewById(R.id.radioGroupOptionsIntro) as RadioGroup
        /*fun bindItems(item: Type) {
        }*/
    }
}
