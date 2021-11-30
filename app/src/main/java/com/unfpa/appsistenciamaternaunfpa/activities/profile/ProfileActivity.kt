package com.unfpa.appsistenciamaternaunfpa.activities.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.activities.MainActivity
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.Type
import com.unfpa.appsistenciamaternaunfpa.adapters.introductory.TypeForCountry
import com.unfpa.appsistenciamaternaunfpa.database.MhealthRoomDatabase
import com.unfpa.appsistenciamaternaunfpa.fragments.profile.*
import com.unfpa.appsistenciamaternaunfpa.utils.Constant
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.json.JSONArray

/**
 * Created by KhyatiShah on 2/27/2020.
 */
class ProfileActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var btnNext: Button? = null
    private var viewPager: androidx.viewpager.widget.ViewPager? = null
    private var UniqueId: String = ""
    var context: Context? = null
    var isFinish: Boolean = false
    var itemlist: MutableList<Type>? = null
    var countrylist: MutableList<TypeForCountry>? = null
    var item_genderlist: MutableList<Type>? = null
    var item_agelist: MutableList<Type>? = null
    var item_interestlist: MutableList<Type>? = null
    private var stringBuilder: StringBuilder? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        //Logging firebase screen
    }

    @SuppressLint("ClickableViewAccessibility", "ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        try {
            toolbar = findViewById(R.id.toolbar)
            list()
            main123()
            listGender()
            listAgeGrp()
            listInterest()
            setSupportActionBar(toolbar)
            toolbar!!.title = ""
            context = this

            btnNext = findViewById(R.id.btnNext)
            //btnNext!!.text = getString(R.string.agree)//"Agree"
            viewPager = findViewById(R.id.viewpagerIntroductory)
            setupViewPager(viewPager!!)
            viewPager!!.setOnTouchListener { _, _ ->
                for (PAGE in 0..viewPager!!.adapter!!.count) {
                    if (viewPager!!.currentItem == PAGE) {
                        viewPager!!.setCurrentItem(PAGE - 1, false)
                        viewPager!!.setCurrentItem(PAGE, false)
                    }
                }
                true
            }
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
            toolbar!!.setNavigationOnClickListener {
                val current = getItem(-1)
                if (current == -1) {
                    // toolbar!!.visibility = View.INVISIBLE
                    launchHomeScreen()
                }
                if (current < 5) {
                    viewPager!!.currentItem = current

                    if (current == 1 || current == 2 || current == 3 || current == 4 || current == 5 || current == 0) {
                        btnNext!!.text = getString(R.string.next)
                    } else {
                        btnNext!!.text = getString(R.string.done)

                    }
                }
                /*if (current == 0) {
                    toolbar!!.visibility = View.INVISIBLE
                }
                if (current < 7 && current != 0) {
                    viewPager!!.currentItem = current
                }*/
            }
            btnNext!!.setOnClickListener {
                val sharedPreference1 = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
                //val Country = sharedPreference1.getString(Constant.ITEM_COUNTRY_CODE, "")
                val CountryCode = sharedPreference1.getString(Constant.ITEM_COUNTRY_CODE, "")
                val Gender = sharedPreference1.getString(Constant.ITEM_GENDER, "")
                val AgeGroup = sharedPreference1.getString(Constant.ITEM_AGE_GROUP, "")
                val Interest = sharedPreference1.getString(Constant.ITEM_INTEREST, "")
                val Education = sharedPreference1.getString(Constant.ITEM_EDUCATION, "")
                // checking for last page
                // if last page home screen will be launched
                val current = getItem(+1)
                if (current < 6) {
                    /*if (current == 1) {
                        //isFinish = true
                        viewPager!!.currentItem = current
                        btnNext!!.text = getString(R.string.next)//"Next"
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_title_arrow)
                    }*/
                    /*if (current == 1) {
                        if (CountryCode == "") {
                            Toast.makeText(this, getString(R.string.fields_are_mandatory), Toast.LENGTH_SHORT).show()
                        } else {
                            viewPager!!.currentItem = current
                            btnNext!!.text = getString(R.string.next)
                        }
                    }*/
                    if (current == 1) {
                        if (Gender == "") {
                            Toast.makeText(this, getString(R.string.fields_are_mandatory), Toast.LENGTH_SHORT).show()
                        } else {
                            viewPager!!.currentItem = current
                            btnNext!!.text = getString(R.string.next)
                        }
                    }
                    if (current == 2) {
                        if (AgeGroup == "") {
                            Toast.makeText(this, getString(R.string.fields_are_mandatory), Toast.LENGTH_SHORT).show()
                        } else {
                            viewPager!!.currentItem = current
                            btnNext!!.text = getString(R.string.next)
                            //btnNext!!.text = getString(R.string.done)//"Done"
                        }
                    }
                    if (current == 3) {
                        if (Interest == "") {
                            Toast.makeText(
                                this,
                                getString(R.string.minimum_one_selected_option_is_necessary_to_proceed),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewPager!!.currentItem = current
                            btnNext!!.text = getString(R.string.done)//"Done"
                        }
                    }
                    if (current == 4) {
                        if (Education == "") {
                            Toast.makeText(this, getString(R.string.fields_are_mandatory), Toast.LENGTH_SHORT).show()
                        } else {
                            val sharedPreference = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
                            var editor = sharedPreference.edit()
                            editor.putBoolean(Constant.INTRO_FLAG, true)
                            editor.apply()
                            editor.commit()
                            checkModulesVisibility(CountryCode.toString())
                            launchHomeScreen()
                            //viewPager!!.currentItem = current
                        }
                    }


                } else {
                    val sharedPreference = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.putBoolean(Constant.INTRO_FLAG, true)
                    editor.apply()
                    editor.commit()
                    checkModulesVisibility(CountryCode.toString())
                    launchHomeScreen()
                }

                //addStoryToDatabase("", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            /*val sharedPreference1 = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE)
            val Country = sharedPreference1.getString(Constant.ITEM_COUNTRY, "")
            val CountryCode = sharedPreference1.getString(Constant.ITEM_COUNTRY_CODE, "")
            val Gender = sharedPreference1.getString(Constant.ITEM_GENDER, "")
            val AgeGroup = sharedPreference1.getString(Constant.ITEM_AGE_GROUP, "")
            val Interest = sharedPreference1.getString(Constant.ITEM_INTEREST, "")
            val Education = sharedPreference1.getString(Constant.ITEM_EDUCATION, "")
            var editor = sharedPreference1.edit()
            editor.clear()
            editor.apply()*/
            if (viewPager!!.currentItem == 1) {
                this.finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun list() {
        itemlist = ArrayList<Type>()
        /*(itemlist as ArrayList<Type>).add(Type("Country 1",false))
        (itemlist as ArrayList<Type>).add(Type("Country 2",false))*/
        val strCountryList =
            "United States of America, Afghanistan, Albania, Algeria, Andorra, Angola, Antigua & Deps, Argentina, Armenia, Australia, Austria, Azerbaijan, Bahamas, Bahrain, Bangladesh, Barbados, Belarus, Belgium, Belize, Benin, Bhutan, Bolivia, Bosnia Herzegovina, Botswana, Brazil, Brunei, Bulgaria, Burkina, Burma, Burundi, Cambodia, Cameroon, Canada, Cape Verde, Central African Rep, Chad, Chile, People's Republic of China, Republic of China, Colombia, Comoros, Democratic Republic of the Congo, Republic of the Congo, Costa Rica,, Croatia, Cuba, Cyprus, Czech Republic, Danzig, Denmark, Djibouti, Dominica, Dominican Republic, East Timor, Ecuador, Egypt, El Salvador, Equatorial Guinea, Eritrea, Estonia, Ethiopia, Fiji, Finland, France, Gabon, Gaza Strip, The Gambia, Georgia, Germany, Ghana, Greece, Grenada, Guatemala, Guinea, Guinea-Bissau, Guyana, Haiti, Holy Roman Empire, Honduras, Hungary, Iceland, India, Indonesia, Iran, Iraq, Republic of Ireland, Israel, Italy, Ivory Coast, Jamaica, Japan, Jonathanland, Jordan, Kazakhstan, Kenya, Kiribati, North Korea, South Korea, Kosovo, Kuwait, Kyrgyzstan, Laos, Latvia, Lebanon, Lesotho, Liberia, Libya, Liechtenstein, Lithuania, Luxembourg, Macedonia, Madagascar, Malawi, Malaysia, Maldives, Mali, Malta, Marshall Islands, Mauritania, Mauritius, Mexico, Micronesia, Moldova, Monaco, Mongolia, Montenegro, Morocco, Mount Athos, Mozambique, Namibia, Nauru, Nepal, Newfoundland, Netherlands, New Zealand, Nicaragua, Niger, Nigeria, Norway, Oman, Ottoman Empire, Pakistan, Palau, Panama, Papua New Guinea, Paraguay, Peru, Philippines, Poland, Portugal, Prussia, Qatar, Romania, Rome, Russian Federation, Rwanda, St Kitts & Nevis, St Lucia, Saint Vincent & the, Grenadines, Samoa, San Marino, Sao Tome & Principe, Saudi Arabia, Senegal, Serbia, Seychelles, Sierra Leone, Singapore, Slovakia, Slovenia, Solomon Islands, Somalia, South Africa, Spain, Sri Lanka, Sudan, Suriname, Swaziland, Sweden, Switzerland, Syria, Tajikistan, Tanzania, Thailand, Togo, Tonga, Trinidad & Tobago, Tunisia, Turkey, Turkmenistan, Tuvalu, Uganda, Ukraine, United Arab Emirates, United Kingdom, Uruguay, Uzbekistan, Vanuatu, Vatican City, Venezuela, Vietnam, Yemen, Zambia, Zimbabwe"
        val result: List<String> = strCountryList.split(",").map { it.trim() }
        for (item in result) {
            (itemlist as ArrayList<Type>).add(Type(item, false))
        }

    }

    fun main123() {
        countrylist = ArrayList<TypeForCountry>()
        val str =
            "[{\"Code\":\"AUS\",\"Name\":\"Australia\"},{\"Code\":\"UAE\",\"Name\":\"United Arab Emirates\"},{\"Code\":\"UK\",\"Name\":\"United Kingdom\"},{\"Code\":\"USA\",\"Name\":\"United States Of America\"},{\"Code\":\"IND\",\"Name\":\"India\"},{\"Code\":\"AD\",\"Name\":\"Andorra\"},{\"Code\":\"AO\",\"Name\":\"Angola\"},{\"Code\":\"AI\",\"Name\":\"Anguilla\"},{\"Code\":\"AQ\",\"Name\":\"Antarctica\"},{\"Code\":\"AG\",\"Name\":\"Antigua and Barbuda\"},{\"Code\":\"AR\",\"Name\":\"Argentina\"},{\"Code\":\"AM\",\"Name\":\"Armenia\"},{\"Code\":\"AW\",\"Name\":\"Aruba\"},{\"Code\":\"AU\",\"Name\":\"Australia\"},{\"Code\":\"AT\",\"Name\":\"Austria\"},{\"Code\":\"AZ\",\"Name\":\"Azerbaijan\"},{\"Code\":\"BS\",\"Name\":\"Bahamas\"},{\"Code\":\"BH\",\"Name\":\"Bahrain\"},{\"Code\":\"BD\",\"Name\":\"Bangladesh\"},{\"Code\":\"BB\",\"Name\":\"Barbados\"},{\"Code\":\"BY\",\"Name\":\"Belarus\"},{\"Code\":\"BE\",\"Name\":\"Belgium\"},{\"Code\":\"BZ\",\"Name\":\"Belize\"},{\"Code\":\"BJ\",\"Name\":\"Benin\"},{\"Code\":\"BM\",\"Name\":\"Bermuda\"},{\"Code\":\"BT\",\"Name\":\"Bhutan\"},{\"Code\":\"BO\",\"Name\":\"Bolivia, Plurinational State of\"},{\"Code\":\"BQ\",\"Name\":\"Bonaire, Sint Eustatius and Saba\"},{\"Code\":\"BA\",\"Name\":\"Bosnia and Herzegovina\"},{\"Code\":\"BW\",\"Name\":\"Botswana\"},{\"Code\":\"BV\",\"Name\":\"Bouvet Island\"},{\"Code\":\"BR\",\"Name\":\"Brazil\"},{\"Code\":\"IO\",\"Name\":\"British Indian Ocean Territory\"},{\"Code\":\"BN\",\"Name\":\"Brunei Darussalam\"},{\"Code\":\"BG\",\"Name\":\"Bulgaria\"},{\"Code\":\"BF\",\"Name\":\"Burkina Faso\"},{\"Code\":\"BI\",\"Name\":\"Burundi\"},{\"Code\":\"KH\",\"Name\":\"Cambodia\"},{\"Code\":\"CM\",\"Name\":\"Cameroon\"},{\"Code\":\"CA\",\"Name\":\"Canada\"},{\"Code\":\"CV\",\"Name\":\"Cape Verde\"},{\"Code\":\"KY\",\"Name\":\"Cayman Islands\"},{\"Code\":\"CF\",\"Name\":\"Central African Republic\"},{\"Code\":\"TD\",\"Name\":\"Chad\"},{\"Code\":\"CL\",\"Name\":\"Chile\"},{\"Code\":\"CN\",\"Name\":\"China\"},{\"Code\":\"CX\",\"Name\":\"Christmas Island\"},{\"Code\":\"CC\",\"Name\":\"Cocos (Keeling) Islands\"},{\"Code\":\"CO\",\"Name\":\"Colombia\"},{\"Code\":\"KM\",\"Name\":\"Comoros\"},{\"Code\":\"CG\",\"Name\":\"Congo\"},{\"Code\":\"CD\",\"Name\":\"Congo, the Democratic Republic of the\"},{\"Code\":\"CK\",\"Name\":\"Cook Islands\"},{\"Code\":\"CR\",\"Name\":\"Costa Rica\"},{\"Code\":\"CI\",\"Name\":\"C\\u00f4te d'Ivoire\"},{\"Code\":\"HR\",\"Name\":\"Croatia\"},{\"Code\":\"CU\",\"Name\":\"Cuba\"},{\"Code\":\"CW\",\"Name\":\"Cura\\u00e7ao\"},{\"Code\":\"CY\",\"Name\":\"Cyprus\"},{\"Code\":\"CZ\",\"Name\":\"Czech Republic\"},{\"Code\":\"DK\",\"Name\":\"Denmark\"},{\"Code\":\"DJ\",\"Name\":\"Djibouti\"},{\"Code\":\"DM\",\"Name\":\"Dominica\"},{\"Code\":\"DO\",\"Name\":\"Dominican Republic\"},{\"Code\":\"EC\",\"Name\":\"Ecuador\"},{\"Code\":\"EG\",\"Name\":\"Egypt\"},{\"Code\":\"SV\",\"Name\":\"El Salvador\"},{\"Code\":\"GQ\",\"Name\":\"Equatorial Guinea\"},{\"Code\":\"ER\",\"Name\":\"Eritrea\"},{\"Code\":\"EE\",\"Name\":\"Estonia\"},{\"Code\":\"ET\",\"Name\":\"Ethiopia\"},{\"Code\":\"FK\",\"Name\":\"Falkland Islands (Malvinas)\"},{\"Code\":\"FO\",\"Name\":\"Faroe Islands\"},{\"Code\":\"FJ\",\"Name\":\"Fiji\"},{\"Code\":\"FI\",\"Name\":\"Finland\"},{\"Code\":\"FR\",\"Name\":\"France\"},{\"Code\":\"GF\",\"Name\":\"French Guiana\"},{\"Code\":\"PF\",\"Name\":\"French Polynesia\"},{\"Code\":\"TF\",\"Name\":\"French Southern Territories\"},{\"Code\":\"GA\",\"Name\":\"Gabon\"},{\"Code\":\"GM\",\"Name\":\"Gambia\"},{\"Code\":\"GE\",\"Name\":\"Georgia\"},{\"Code\":\"DE\",\"Name\":\"Germany\"},{\"Code\":\"GH\",\"Name\":\"Ghana\"},{\"Code\":\"GI\",\"Name\":\"Gibraltar\"},{\"Code\":\"GR\",\"Name\":\"Greece\"},{\"Code\":\"GL\",\"Name\":\"Greenland\"},{\"Code\":\"GD\",\"Name\":\"Grenada\"},{\"Code\":\"GP\",\"Name\":\"Guadeloupe\"},{\"Code\":\"GU\",\"Name\":\"Guam\"},{\"Code\":\"GT\",\"Name\":\"Guatemala\"},{\"Code\":\"GG\",\"Name\":\"Guernsey\"},{\"Code\":\"GN\",\"Name\":\"Guinea\"},{\"Code\":\"GW\",\"Name\":\"Guinea-Bissau\"},{\"Code\":\"GY\",\"Name\":\"Guyana\"},{\"Code\":\"HT\",\"Name\":\"Haiti\"},{\"Code\":\"HM\",\"Name\":\"Heard Island and McDonald Islands\"},{\"Code\":\"VA\",\"Name\":\"Holy See (Vatican City State)\"},{\"Code\":\"HN\",\"Name\":\"Honduras\"},{\"Code\":\"HK\",\"Name\":\"Hong Kong\"},{\"Code\":\"HU\",\"Name\":\"Hungary\"},{\"Code\":\"IS\",\"Name\":\"Iceland\"},{\"Code\":\"IN\",\"Name\":\"India\"},{\"Code\":\"ID\",\"Name\":\"Indonesia\"},{\"Code\":\"IR\",\"Name\":\"Iran, Islamic Republic of\"},{\"Code\":\"IQ\",\"Name\":\"Iraq\"},{\"Code\":\"IE\",\"Name\":\"Ireland\"},{\"Code\":\"IM\",\"Name\":\"Isle of Man\"},{\"Code\":\"IL\",\"Name\":\"Israel\"},{\"Code\":\"IT\",\"Name\":\"Italy\"},{\"Code\":\"JM\",\"Name\":\"Jamaica\"},{\"Code\":\"JP\",\"Name\":\"Japan\"},{\"Code\":\"JE\",\"Name\":\"Jersey\"},{\"Code\":\"JO\",\"Name\":\"Jordan\"},{\"Code\":\"KZ\",\"Name\":\"Kazakhstan\"},{\"Code\":\"KE\",\"Name\":\"Kenya\"},{\"Code\":\"KI\",\"Name\":\"Kiribati\"},{\"Code\":\"KP\",\"Name\":\"Korea, Democratic People's Republic of\"},{\"Code\":\"KR\",\"Name\":\"Korea, Republic of\"},{\"Code\":\"KW\",\"Name\":\"Kuwait\"},{\"Code\":\"KG\",\"Name\":\"Kyrgyzstan\"},{\"Code\":\"LA\",\"Name\":\"Lao People's Democratic Republic\"},{\"Code\":\"LV\",\"Name\":\"Latvia\"},{\"Code\":\"LB\",\"Name\":\"Lebanon\"},{\"Code\":\"LS\",\"Name\":\"Lesotho\"},{\"Code\":\"LR\",\"Name\":\"Liberia\"},{\"Code\":\"LY\",\"Name\":\"Libya\"},{\"Code\":\"LI\",\"Name\":\"Liechtenstein\"},{\"Code\":\"LT\",\"Name\":\"Lithuania\"},{\"Code\":\"LU\",\"Name\":\"Luxembourg\"},{\"Code\":\"MO\",\"Name\":\"Macao\"},{\"Code\":\"MK\",\"Name\":\"Macedonia, the Former Yugoslav Republic of\"},{\"Code\":\"MG\",\"Name\":\"Madagascar\"},{\"Code\":\"MW\",\"Name\":\"Malawi\"},{\"Code\":\"MY\",\"Name\":\"Malaysia\"},{\"Code\":\"MV\",\"Name\":\"Maldives\"},{\"Code\":\"ML\",\"Name\":\"Mali\"},{\"Code\":\"MT\",\"Name\":\"Malta\"},{\"Code\":\"MH\",\"Name\":\"Marshall Islands\"},{\"Code\":\"MQ\",\"Name\":\"Martinique\"},{\"Code\":\"MR\",\"Name\":\"Mauritania\"},{\"Code\":\"MU\",\"Name\":\"Mauritius\"},{\"Code\":\"YT\",\"Name\":\"Mayotte\"},{\"Code\":\"MX\",\"Name\":\"Mexico\"},{\"Code\":\"FM\",\"Name\":\"Micronesia, Federated States of\"},{\"Code\":\"MD\",\"Name\":\"Moldova, Republic of\"},{\"Code\":\"MC\",\"Name\":\"Monaco\"},{\"Code\":\"MN\",\"Name\":\"Mongolia\"},{\"Code\":\"ME\",\"Name\":\"Montenegro\"},{\"Code\":\"MS\",\"Name\":\"Montserrat\"},{\"Code\":\"MA\",\"Name\":\"Morocco\"},{\"Code\":\"MZ\",\"Name\":\"Mozambique\"},{\"Code\":\"MM\",\"Name\":\"Myanmar\"},{\"Code\":\"NA\",\"Name\":\"Namibia\"},{\"Code\":\"NR\",\"Name\":\"Nauru\"},{\"Code\":\"NP\",\"Name\":\"Nepal\"},{\"Code\":\"NL\",\"Name\":\"Netherlands\"},{\"Code\":\"NC\",\"Name\":\"New Caledonia\"},{\"Code\":\"NZ\",\"Name\":\"New Zealand\"},{\"Code\":\"NI\",\"Name\":\"Nicaragua\"},{\"Code\":\"NE\",\"Name\":\"Niger\"},{\"Code\":\"NG\",\"Name\":\"Nigeria\"},{\"Code\":\"NU\",\"Name\":\"Niue\"},{\"Code\":\"NF\",\"Name\":\"Norfolk Island\"},{\"Code\":\"MP\",\"Name\":\"Northern Mariana Islands\"},{\"Code\":\"NO\",\"Name\":\"Norway\"},{\"Code\":\"OM\",\"Name\":\"Oman\"},{\"Code\":\"PK\",\"Name\":\"Pakistan\"},{\"Code\":\"PW\",\"Name\":\"Palau\"},{\"Code\":\"PS\",\"Name\":\"Palestine, State of\"},{\"Code\":\"PA\",\"Name\":\"Panama\"},{\"Code\":\"PG\",\"Name\":\"Papua New Guinea\"},{\"Code\":\"PY\",\"Name\":\"Paraguay\"},{\"Code\":\"PE\",\"Name\":\"Peru\"},{\"Code\":\"PH\",\"Name\":\"Philippines\"},{\"Code\":\"PN\",\"Name\":\"Pitcairn\"},{\"Code\":\"PL\",\"Name\":\"Poland\"},{\"Code\":\"PT\",\"Name\":\"Portugal\"},{\"Code\":\"PR\",\"Name\":\"Puerto Rico\"},{\"Code\":\"QA\",\"Name\":\"Qatar\"},{\"Code\":\"RE\",\"Name\":\"R\\u00e9union\"},{\"Code\":\"RO\",\"Name\":\"Romania\"},{\"Code\":\"RU\",\"Name\":\"Russian Federation\"},{\"Code\":\"RW\",\"Name\":\"Rwanda\"},{\"Code\":\"BL\",\"Name\":\"Saint Barth\\u00e9lemy\"},{\"Code\":\"SH\",\"Name\":\"Saint Helena, Ascension and Tristan da Cunha\"},{\"Code\":\"KN\",\"Name\":\"Saint Kitts and Nevis\"},{\"Code\":\"LC\",\"Name\":\"Saint Lucia\"},{\"Code\":\"MF\",\"Name\":\"Saint Martin (French part)\"},{\"Code\":\"PM\",\"Name\":\"Saint Pierre and Miquelon\"},{\"Code\":\"VC\",\"Name\":\"Saint Vincent and the Grenadines\"},{\"Code\":\"WS\",\"Name\":\"Samoa\"},{\"Code\":\"SM\",\"Name\":\"San Marino\"},{\"Code\":\"ST\",\"Name\":\"Sao Tome and Principe\"},{\"Code\":\"SA\",\"Name\":\"Saudi Arabia\"},{\"Code\":\"SN\",\"Name\":\"Senegal\"},{\"Code\":\"RS\",\"Name\":\"Serbia\"},{\"Code\":\"SC\",\"Name\":\"Seychelles\"},{\"Code\":\"SL\",\"Name\":\"Sierra Leone\"},{\"Code\":\"SG\",\"Name\":\"Singapore\"},{\"Code\":\"SX\",\"Name\":\"Sint Maarten (Dutch part)\"},{\"Code\":\"SK\",\"Name\":\"Slovakia\"},{\"Code\":\"SI\",\"Name\":\"Slovenia\"},{\"Code\":\"SB\",\"Name\":\"Solomon Islands\"},{\"Code\":\"SO\",\"Name\":\"Somalia\"},{\"Code\":\"ZA\",\"Name\":\"South Africa\"},{\"Code\":\"GS\",\"Name\":\"South Georgia and the South Sandwich Islands\"},{\"Code\":\"SS\",\"Name\":\"South Sudan\"},{\"Code\":\"ES\",\"Name\":\"Spain\"},{\"Code\":\"LK\",\"Name\":\"Sri Lanka\"},{\"Code\":\"SD\",\"Name\":\"Sudan\"},{\"Code\":\"SR\",\"Name\":\"Suriname\"},{\"Code\":\"SJ\",\"Name\":\"Svalbard and Jan Mayen\"},{\"Code\":\"SZ\",\"Name\":\"Swaziland\"},{\"Code\":\"SE\",\"Name\":\"Sweden\"},{\"Code\":\"CH\",\"Name\":\"Switzerland\"},{\"Code\":\"SY\",\"Name\":\"Syrian Arab Republic\"},{\"Code\":\"TW\",\"Name\":\"Taiwan, Province of China\"},{\"Code\":\"TJ\",\"Name\":\"Tajikistan\"},{\"Code\":\"TZ\",\"Name\":\"Tanzania, United Republic of\"},{\"Code\":\"TH\",\"Name\":\"Thailand\"},{\"Code\":\"TL\",\"Name\":\"Timor-Leste\"},{\"Code\":\"TG\",\"Name\":\"Togo\"},{\"Code\":\"TK\",\"Name\":\"Tokelau\"},{\"Code\":\"TO\",\"Name\":\"Tonga\"},{\"Code\":\"TT\",\"Name\":\"Trinidad and Tobago\"},{\"Code\":\"TN\",\"Name\":\"Tunisia\"},{\"Code\":\"TR\",\"Name\":\"Turkey\"},{\"Code\":\"TM\",\"Name\":\"Turkmenistan\"},{\"Code\":\"TC\",\"Name\":\"Turks and Caicos Islands\"},{\"Code\":\"TV\",\"Name\":\"Tuvalu\"},{\"Code\":\"UG\",\"Name\":\"Uganda\"},{\"Code\":\"UA\",\"Name\":\"Ukraine\"},{\"Code\":\"AE\",\"Name\":\"United Arab Emirates\"},{\"Code\":\"GB\",\"Name\":\"United Kingdom\"},{\"Code\":\"US\",\"Name\":\"United States\"},{\"Code\":\"UM\",\"Name\":\"United States Minor Outlying Islands\"},{\"Code\":\"UY\",\"Name\":\"Uruguay\"},{\"Code\":\"UZ\",\"Name\":\"Uzbekistan\"},{\"Code\":\"VU\",\"Name\":\"Vanuatu\"},{\"Code\":\"VE\",\"Name\":\"Venezuela, Bolivarian Republic of\"},{\"Code\":\"VN\",\"Name\":\"Viet Nam\"},{\"Code\":\"VG\",\"Name\":\"Virgin Islands, British\"},{\"Code\":\"VI\",\"Name\":\"Virgin Islands, U.S.\"},{\"Code\":\"WF\",\"Name\":\"Wallis and Futuna\"},{\"Code\":\"EH\",\"Name\":\"Western Sahara\"},{\"Code\":\"YE\",\"Name\":\"Yemen\"},{\"Code\":\"ZM\",\"Name\":\"Zambia\"},{\"Code\":\"ZW\",\"Name\":\"Zimbabwe\"}]"
        //writeJSONtoFile(fileName)
        countrylist = Gson().fromJson(str, Array<TypeForCountry>::class.java).toMutableList()


        /*for (item in  contentList) {
            try {
                (countrylist as ArrayList<TypeForCountry>).add(TypeForCountry(item.itemName,item.itemCode,false))
                //srhContentCategoryDAO.insert(contentList.get(i))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }*/
    }


    private fun listGender() {
        item_genderlist = ArrayList<Type>()
        //val strList = "Male,Female,Other / Self Described"
        val strList =
            getString(R.string.male) + "," + getString(R.string.female) + "," + getString(R.string.self_described)
        val result: List<String> = strList.split(",").map { it.trim() }
        for (item in result) {
            (item_genderlist as ArrayList<Type>).add(Type(item, false))
        }
    }

    private fun listAgeGrp() {
        item_agelist = ArrayList<Type>()
        //val strList = "14 - 18 years old,18 - 24 years old,24 or above,any"
        val strList =
            getString(R.string.age_14_18) + "," + getString(R.string.age_18_24) + "," + getString(R.string.age_24_above) + "," + getString(
                R.string.age_any
            )
        val result: List<String> = strList.split(",").map { it.trim() }
        for (item in result) {
            (item_agelist as ArrayList<Type>).add(Type(item, false))
        }
    }

    private fun listInterest() {
        item_interestlist = ArrayList<Type>()
        //val strList = "Learn more about sexual and reproductive health topics.,Get help fining a clinic.,Talk to someone about my questions.,Share my Story.,Something else."
        val strList = getString(R.string.interest_1) + "," +
                getString(R.string.interest_2) + "," +
                getString(R.string.interest_3) + "," +
                getString(R.string.interest_4) + "," +
                getString(R.string.interest_5)
        val result: List<String> = strList.split(",").map { it.trim() }
        for (item in result) {
            (item_interestlist as ArrayList<Type>).add(Type(item, false))
        }
    }

    public fun getGenderList(): MutableList<Type>? {

        return item_genderlist
    }

    public fun getAgeGrpList(): MutableList<Type>? {

        return item_agelist
    }

    public fun getInterestList(): MutableList<Type>? {

        return item_interestlist
    }

    fun getEducationList(): ArrayList<Type> {
        val arrTypeEdu = ArrayList<Type>()
        val eduLevel = arrayListOf<String>(*resources.getStringArray(R.array.education_level_intro_que))
        for (i in 0 until eduLevel.size) {
            arrTypeEdu.add(Type(eduLevel.get(i), false))
        }
        return arrTypeEdu
    }


    private fun setupViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        val adapter = sectionsPagerAdapter(this.supportFragmentManager)
        // adapter.addFragment(SlideAInfoFragment(), "")
        // adapter.addFragment(SlideBCountryFragment(), "")
        //adapter.addFragment(SlideCDistrictFragment(), "")
        adapter.addFragment(SlideDGenderFragment(), "")
        adapter.addFragment(SlideEAgeGroupFragment(), "")
        adapter.addFragment(SlideFInterestedFragement(), "")
        adapter.addFragment(SlideGEducationFragement(), "")
        viewPager.adapter = adapter
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        //prefManager?.setFirstTimeLaunch(false)

        startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        finish()
    }

    private fun checkModulesVisibility(CountryCode: String) {
        try {
            val countryOfficeDAO = MhealthRoomDatabase.getAppDataBase(this.applicationContext)!!.countryOfficeDAO()
            val countryCodeRequest = countryOfficeDAO.getModuleVisibility(CountryCode)
            val field_enable_health_manager: String = countryCodeRequest.field_enable_health_manager
            val field_enable_live_chat: String = countryCodeRequest.field_enable_live_chat
            val field_enable_service_locator: String = countryCodeRequest.field_enable_service_locator
            val field_language: String = countryCodeRequest.field_language
            val field_health_management_export = countryCodeRequest.field_health_management_export.toString()
            val jarr = JSONArray(field_health_management_export)
            val field_health_enabled: String = jarr.getJSONObject(0).getString("enabled")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal inner class sectionsPagerAdapter(manager: androidx.fragment.app.FragmentManager) :
        androidx.fragment.app.FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


}