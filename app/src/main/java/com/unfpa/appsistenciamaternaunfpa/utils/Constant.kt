package com.unfpa.appsistenciamaternaunfpa.utils

object Constant {
    val DB_NAME = "mhealth_database.db"
    val PREF_UNIQUE_ID = "UNIQUE_ID"
    val PREF_NAME = "MY_PREF"
    val PREF_AGE_GROUP = "AgeGroup"
    val PREF_GENDER = "Gender"
    val PREF_COUNTRY = "CountryCode"
    val NO_OF_DAYS = "NumberOfDays"
    val SELECTED_DATE = "SelectedDate"
    val PERIOD_DATE_FORMAT = "dd-MM-yyyy"
    val MEDICINE_TIME_FORMAT_INPUT = "HH:mm"
    val MEDICINE_TIME_FORMAT_TARGET = "hh:mm aa"
    val CENTER_ID = "center_id"
    val CENTER_NAME = "center_name"
    //val APPOINTMENT_DISPLAY_DATE_FORMAT = "dd MMM YYYY"
    val APPOINTMENT_DISPLAY_DATE_FORMAT = "dd MMM yyyy"
    val SERVER_TIMESTEMP = "ServerTimestemp"
    val PREF_CHAT_UID = "ChatUID"
    val PREF_CHAT_DISP_NAME = "ChatDispName"
    val PREF_IS_FIRST_TIME = "isFirstTime"
    val PREF_CO_CODE = "coCode"
    val ITEM_COUNTRY_CODE = "CountryCode"
    val PREF_LANGUAGE_CODE = "languageCode"

    //Screen names for firebase
    val APP_OPEN = "App_Open"
    val USER_PROFILE_REG = "User_Profile_Registration"
    val SRH_CONTENT_VIEW = "SRH_Content_View"
    val SRH_CONTENT_SHARE = "SRH_Content_Share"
    val SRH_CONTENT_SEARCH = "SRH_Content_Search"
    val SRH_CONTENT_FAVORITE = "SRH_Content_Favorite"
    val USER_QUIZ_TAKE = "User_Quiz_Take"
    val USER_QUIZ_RESPOND = "User_Quiz_Respond"
    val MENSTRUAL_TRACKER = "Menstrual_Tracker"
    val OTHER_HEALTH_TOOL = "Other_Health_Tool"
    val SERVICE_PROVIDER_SEARCH = "Service_Provider_Search"
    val SERVICE_PROVIDER_SHARE = "Service_Provider_Share"
    val SERVICE_PROVIDER_FEEDBACK = "Service_Provider_Feedback"
    val STORY_SUBMIT = "Story_Submit"
    val CALL_KNOWLEDGEABLE_PERSON = "Call_Knowledgeable_person"
    val CHAT_KNOWLEDGEABLE_PERSON = "Chat_Knowledgeable_person"

    //constant for chat module
    val MESSAGE_TYPE_INCOMING = "Incoming"
    val MESSAGE_TYPE_OUTGOING = "Outgoing"
    val NOTIFICATION_TIMEOUT: Long = 300000 // 5 min = 5 * 60 * 1000 ms
    val DISCONNECT_TIMEOUT: Long = 300000
    val CHANNEL_ID = "com.unfpa.appsistenciamaternaunfpa.notification.CHANNEL_ID"
    val CHANNEL_NAME = "mHealth Notification"

    //constant for notification
    val NOTIFICATION_TYPE_MEDICINE = "Noti_Medi"
    val NOTIFICATION_TYPE_APPOINTMENT = "Noti_Appo"
    val NOTIFICATION_ITEM_ID = "NotificationItemId"
    val NOTIFICATION_ID = "NotificationId"
    val NOTIFICATION_TITLE = "NotificationTitle"
    val NOTIFICATION_TYPE = "NotificationType"
    val NOTIFICATION_SUB_TITLE = "NotificationSubTitle"

    //Intro screen Item Name
    //val ITEM_COUNTRY = "Country"

    val ITEM_DISTRICT = "District"
    val ITEM_GENDER = "Gender"
    val ITEM_AGE_GROUP = "AgeGroup"
    val ITEM_INTEREST = "Interest"
    val ITEM_EDUCATION = "Education"
    val INTRO_FLAG = "IntroFlag"

    //Firebase node names
    val KNOWLEDGEABLE_PERSON_NODE = "KnowledgeablePersons"
    val MESSAGES_NODE = "Messages"
    val USERS_NODE = "Users"
    val QUEUE_NODE = "Que"
    val CONVERSATION_NODE = "Con"
    val KNOWLEDGEABLE_PERSON_UID = "UID"
    val KNOWLEDGEABLE_PERSON_NAME = "Name"
    val KNOWLEDGEABLE_PERSON_COUNTRY_CODE = "CountryCode"
    val MESSAGE_COUNT = "messageCount"

    //Constant for ReqName
    val REQ_COUNT_OFFICE = "CountryOfficeList"
    val REQ_CATEGORIES = "ContentCategories"
    val REQ_CONTENT_LIST = "ContentList"
    val REQ_SERVICE_CENTER_DETAIL = "ServiceCenterDetail"
    val REQ_SERVICE_LIST = "ServiceList"
    val REQ_QUIZ = "Quiz"
    val REQ_COUNTRY_LIST = "CountryList"

    //constant for taged list fragment
    val TAG_NAME = "tagName"
    val FRAG_TAGED_NAME = "taggedFrag"

    //constant pref for data users
    val PREF_USER_ID = "UserId"
    val PREF_USER_FIRSTNAME = "UserFirstName"
    val PREF_USER_LASTNAME = "UserLastName"
    val PREF_USER_EMAIL = "UserEmail"
    val TYPE_USER = "TypeUser"


}