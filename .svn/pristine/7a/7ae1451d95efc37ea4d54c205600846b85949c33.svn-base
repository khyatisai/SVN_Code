package com.unfpa.appsistenciamaternaunfpa.database

import android.content.Context
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryList
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryOfficeSettingEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.QuizRequest
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import org.json.JSONArray
import org.json.JSONObject
import com.unfpa.appsistenciamaternaunfpa.database.entity.myvoice.MyVoiceEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.personaldetails.PersonalDetailsEntity


class DBHelper {
    companion object {
        fun insertContentMaster(contentList: List<SRHContent>, context: Context) {
            val contentMasterDAO = MhealthRoomDatabase.getAppDataBase(context)!!.contentMasterDAO()

            for (i in 0 until contentList.size) {
                try {
                    contentMasterDAO.insert(contentList.get(i))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        /*fun insertContentDetail(contentList: List<ContentDetail>, context: Context) {
            val contentDetailDAO = MhealthRoomDatabase.getAppDataBase(context)!!.contentDetailDAO()
            for (i in 0 until contentList.size) {
                try {
                    contentDetailDAO.insert(contentList.get(i))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }*/

        fun insertContentCategory(categoryList: List<SRHContentCategory>, context: Context) {
            val srhContentCategoryDAO = MhealthRoomDatabase.getAppDataBase(context)!!.SRHContentCategoryDAO()

            for (i in 0 until categoryList.size) {
                try {
                    srhContentCategoryDAO.insert(categoryList.get(i))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //added by 35251
        fun insertCenterContentDetails(centerList: List<ServiceCenterDetailEntity>, context: Context) {
            val serviceCenterDetailDAO = MhealthRoomDatabase.getAppDataBase(context)!!.serviceCenterDetailDAO()

            for (i in 0 until centerList.size) {
                try {
                    serviceCenterDetailDAO.insert(centerList[i])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //added by 35251
        fun insertMyServiceDetails(serviceList: List<MyServiceEntity>, context: Context) {
            val myServiceDAO = MhealthRoomDatabase.getAppDataBase(context)!!.myServiceDAO()

            for (i in 0 until serviceList.size) {
                try {
                    myServiceDAO.insert(serviceList[i])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //added by 35251
        fun insertMVoiceDetails(voiceList: List<MyVoiceEntity>, context: Context) {
            val myVoiceDAO = MhealthRoomDatabase.getAppDataBase(context)!!.myVoiceDAO()

            for (i in 0 until voiceList.size) {
                try {
                    myVoiceDAO.insert(voiceList[i])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //added by 35251
        fun updateMVoiceDetails(voiceList: List<MyVoiceEntity>, context: Context) {
            val myVoiceDAO = MhealthRoomDatabase.getAppDataBase(context)!!.myVoiceDAO()

            for (i in 0 until voiceList.size) {
                try {
                    myVoiceDAO.update(voiceList[i])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //added by 35251
        fun insertPersonalDetails(personalDetailsList: List<PersonalDetailsEntity>, context: Context) {
            val personalDetailsDAO = MhealthRoomDatabase.getAppDataBase(context)!!.personalDetailsDAO()

            for (i in 0 until personalDetailsList.size) {
                try {
                    personalDetailsDAO.insert(personalDetailsList[i])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        //added by 35251
        fun updatePersonalDetails(personalDetailsList: List<PersonalDetailsEntity>, context: Context) {
            val personalDetailsDAO = MhealthRoomDatabase.getAppDataBase(context)!!.personalDetailsDAO()

            for (i in 0 until personalDetailsList.size) {
                try {
                    personalDetailsDAO.update(personalDetailsList[i])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        fun insertQuizRequest(rawResponse: String, context: Context) {
            try {
                val quizRequestDAO = MhealthRoomDatabase.getAppDataBase(context)!!.quizRequestDAO()
                val jsonArrQuiz = JSONArray(rawResponse)
                for (i in 0 until jsonArrQuiz.length()) {
                    val jsonQuiz: JSONObject = jsonArrQuiz.getJSONObject(i)
                    val categoryId = jsonQuiz.get("field_thematic_area_id")
                    val quizId = jsonQuiz.get("nid")
                    val quizRequest = QuizRequest(
                        categoryId = categoryId.toString(),
                        quizId = quizId.toString(),
                        jsonQuiz = jsonQuiz.toString()
                    )
                    quizRequestDAO.insert(quizRequest)
                }
                quizRequestDAO.insert(QuizRequest("28", "883", ""))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //added by 35251
        fun insertCountryOfficeDetails(rawResponse: String, context: Context) {
            val countryOfficeDAO = MhealthRoomDatabase.getAppDataBase(context)!!.countryOfficeDAO()
            val jsonArrCountryOffice = JSONArray(rawResponse)
            for (i in 0 until jsonArrCountryOffice.length()) {
                val jsonCountryOffice: JSONObject = jsonArrCountryOffice.getJSONObject(i)
                val field_country = jsonCountryOffice.get("field_country")
                val field_client_code = jsonCountryOffice.get("field_client_code")
                val field_image = jsonCountryOffice.get("field_image")
                val title = jsonCountryOffice.get("title")
                val field_subtitle = jsonCountryOffice.get("field_subtitle")
                val field_enable_health_manager = jsonCountryOffice.get("field_enable_health_manager")
                val field_enable_live_chat = jsonCountryOffice.get("field_enable_live_chat")
                val field_enable_service_locator = jsonCountryOffice.get("field_enable_service_locator")
                val field_sms_getway_url = jsonCountryOffice.get("field_sms_getway_url")
                val field_sms_username = jsonCountryOffice.get("field_sms_username")
                val field_sms_auth = jsonCountryOffice.get("field_sms_auth")
                val field_maximum_distance = jsonCountryOffice.get("field_maximum_distance")
                val field_language = jsonCountryOffice.get("langcode")
                val field_health_management_export = jsonCountryOffice.getJSONArray("field_health_management_export")
                val field_contact_number = jsonCountryOffice.get("field_contact_number")
                val field_country_export = jsonCountryOffice.get("field_country_export")
                val countryOfficeSettingEntity = CountryOfficeSettingEntity(
                    field_country = field_country.toString(),
                    field_client_code = field_client_code.toString(),
                    field_image = field_image.toString(),
                    title = title.toString(),
                    field_subtitle = field_subtitle.toString(),
                    field_enable_health_manager = field_enable_health_manager.toString(),
                    field_enable_live_chat = field_enable_live_chat.toString(),
                    field_enable_service_locator = field_enable_service_locator.toString(),
                    field_sms_getway_url = field_sms_getway_url.toString(),
                    field_sms_username = field_sms_username.toString(),
                    field_sms_auth = field_sms_auth.toString(),
                    field_maximum_distance = field_maximum_distance.toString(),
                    field_language = field_language.toString(),
                    field_health_management_export = field_health_management_export.toString(),
                    field_contact_number = field_contact_number.toString(),
                    field_country_export = field_country_export.toString()
                )
                countryOfficeDAO.insert(countryOfficeSettingEntity)
            }
        }

        fun insertCountries(countryList: List<CountryList>, context: Context) {
            val srhContentCategoryDAO = MhealthRoomDatabase.getAppDataBase(context)!!.countryListDAO()

            for (i in 0 until countryList.size) {
                try {
                    srhContentCategoryDAO.insert(countryList.get(i))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}