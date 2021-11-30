package com.unfpa.appsistenciamaternaunfpa.fragments.my_voice.chat

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.adapters.my_voice.KnowledgeablePersonAdapter
import com.unfpa.appsistenciamaternaunfpa.models.KnowledgeablePerson
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import com.unfpa.appsistenciamaternaunfpa.utils.FirestoreUtil
import com.unfpa.appsistenciamaternaunfpa.api_controller.Listeners.OnKnowledgeablePersonFetched
import kotlinx.android.synthetic.main.fragment_chat_people.*
import kotlinx.android.synthetic.main.fragment_chat_people.progressBar
import kotlinx.android.synthetic.main.popup_display_name.*


/**
 * Created by KhyatiShah on 11/22/2019.
 */
class ChatPeopleFragment : androidx.fragment.app.Fragment(),
    OnKnowledgeablePersonFetched {

    private lateinit var auth: FirebaseAuth
    private lateinit var knowledgeablePersonAdapter: KnowledgeablePersonAdapter
    private var listKPCont = ArrayList<KnowledgeablePerson>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_chat_people, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = activity!!.getString(R.string.chat)
        lstKnowledgeablePersons.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context)
        knowledgeablePersonAdapter = KnowledgeablePersonAdapter(this)
        lstKnowledgeablePersons.adapter = knowledgeablePersonAdapter

        //chkCountrySelection()
        showDisplayNamePopup()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu != null) {
            menu!!.findItem(R.id.notification).isVisible = false
            menu!!.findItem(R.id.home).isVisible = true
        }
    }

    fun loginAsAnonymousFirebase() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        auth.signInAnonymously()
            .addOnCompleteListener(this!!.activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("", "signInAnonymously:success")
                    val user = auth.currentUser
                    val uId = user!!.uid
                    //FirestoreUtil.writeNewUser(uId)
                    AppUtils.setChatUID(this.activity!!, uId)
                    //getUserList()
                    //create_chat_channel(uId, "Q0hDKrA8B2P2GGY3YfhGYROrVos1", "Hello")
                    FirestoreUtil.getKnowledgeablePersons(this)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("", "signInAnonymously:failure", task.exception)

                }

                // ...
            }

    }

    fun showDisplayNamePopup() {
        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.popup_display_name)
            dialog.edtDispName.setText(AppUtils.getDisplayName(this!!.activity!!))
            dialog.txtSubmit.setOnClickListener {
                if (TextUtils.isEmpty(dialog.edtDispName.text.toString().trim())) {
                    dialog.edtDispName.error = activity!!.getString(R.string.errDisplayName)
                } else {
                    AppUtils.setDisplayName(this!!.activity!!, dialog.edtDispName.text.toString().trim())
                    dialog.dismiss()
                    loginAsAnonymousFirebase()
                    progressBar.visibility = View.VISIBLE
                }
            }
            dialog.show()
        }

    }

    fun getUserList() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseDatabase.getInstance()
        val users = db.getReference("default").child("users")
        // val progressRef = rootRef.child("Users")
        // Read from the database
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (messageSnapshot in dataSnapshot.children) {
                    val uId = messageSnapshot.key
                    val email = messageSnapshot.child("meta").child("email").value.toString()
                    //val message = messageSnapshot.child("message").value as String?
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException())
            }
        })


    }

    override fun onComplete(lstKnowledgeablePerson: ArrayList<KnowledgeablePerson>) {
        val sharedPrefs = (activity as AppCompatActivity).getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
        val countryCode = sharedPrefs.getString(Constant.ITEM_COUNTRY_CODE, "")
        listKPCont.clear()

        for (i in lstKnowledgeablePerson) {
            if (i.countryCode.equals(countryCode)) {
                listKPCont.add(i)
            }
        }
        knowledgeablePersonAdapter.setContentList(listKPCont)
        progressBar.visibility = View.GONE
        if (listKPCont.isEmpty()) {
            txtNoKP.visibility = View.VISIBLE
            lstKnowledgeablePersons.visibility = View.GONE
        }

    }

    /* fun chkCountrySelection() {
         val sharedPrefs = (activity as AppCompatActivity).getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
         // val country = sharedPrefs.getString(Constant.ITEM_COUNTRY, "")
         val countryCode = sharedPrefs.getString(Constant.ITEM_COUNTRY_CODE, "")
         if (TextUtils.isEmpty(countryCode)) {
             val dialog = Dialog(activity)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             dialog.setCancelable(false)
             dialog.setContentView(R.layout.popup_country_selection)
             dialog.recyclerCountry.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
             dialog.btnSubmit.setOnClickListener {
                 if (!TextUtils.isEmpty(sharedPrefs.getString(Constant.ITEM_COUNTRY_CODE, ""))) {
                     dialog.dismiss()
                     showDisplayNamePopup()
                 } else {
                     Toast.makeText(
                         this.activity,
                         (activity as AppCompatActivity).getString(R.string.select_your_country),
                         Toast.LENGTH_LONG
                     )
                         .show()
                 }
             }

             val countryListDAO =
                 MhealthRoomDatabase.getAppDataBase(this.activity!!.applicationContext)!!.countryListDAO()
             val listCountry: List<CountryList> = countryListDAO?.getAllCountry()
             val arrTypeForCountry = ArrayList<TypeForCountry>()
             for (i in listCountry) {
                 arrTypeForCountry.add(TypeForCountry(i.countryName, i.countryCode, false))
             }
             *//* val adapter = IntroCountryAdapter(
                 arrTypeForCountry,
                 this.activity!!,
                 Constant.ITEM_COUNTRY,
                 Constant.ITEM_COUNTRY_CODE
             )
             dialog.recyclerCountry.adapter = adapter*//*
            dialog.show()

        } else {
            showDisplayNamePopup()
        }
    }*/
}