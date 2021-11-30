package com.unfpa.appsistenciamaternaunfpa.api_controller

object EndPoints {
    //val URL_ROOT = "https://api.github.com/search/users?q=eyehunt"
    //val URL_ADD_ARTIST = URL_ROOT + "addartist"
    //val URL_GET_ARTIST = URL_ROOT + "getartists"

    //val URL_ROOT = "http://mhealth-dev-01.unfpa.org"
    val URL_ROOT = "https://mhsp.unfpa.org"

    //val URL_HEROKU = "https://api-unfpa.herokuapp.com"
    //val URL_HEROKU = "http://143.208.181.241"
    val URL_HEROKU = "http://34.91.117.132:33120"
//    val URL_HEROKU = "https://new-api-unfpa.herokuapp.com"

    val URL_SRH_CONTENT_LIST = URL_ROOT + "/mhealth/srh_content_listing"
    val URL_SRH_CONTENT_DESC = URL_ROOT + "/mhealth/srh_content_detail/"
    val URL_SRH_CONTENT_CATGORY = URL_ROOT + "/mhealth/srh_categories"

    //    val URL_SERVICE_CENTER_DETAILS = "$URL_ROOT/mhealth/srh_service_provider_listing" //added by 35251
    val URL_SERVICE_CENTER_DETAILS = "$URL_HEROKU/api/v1/centers" //add
    val URL_MY_SERVICE_DETAILS = "$URL_ROOT/mhealth/srh_service" //added by 35251
    val URL_GET_TOKEN = "$URL_ROOT/mhealth/rest/session/token" //added by 35251
    val URL_POST_FOR_FEEDBACK =
        "$URL_ROOT/mhealth/node?_format=hal_json" //added by 35251 http://mhealth-dev-01.unfpa.org/mhealth/node?_format=hal_json
    val URL_QUIZ_LISTING = URL_ROOT + "/mhealth/quiz_listing"
    val URL_MY_VOICE = "$URL_ROOT/mhealth/api/custom?_format=hal_json"
    val URL_POST_QUIZ = URL_ROOT + "/mhealth/api/quiz?_format=hal_json"
    val URL_COUNTRY_OFFICE_LISTING = URL_ROOT + "/mhealth/country_office_listing"
    val URL_SYNC_TIMESTEMP = URL_ROOT + "/mhealth/sync_content"
    val URL_COUNTRY_LIST = URL_ROOT + "/mhealth/countries"

}