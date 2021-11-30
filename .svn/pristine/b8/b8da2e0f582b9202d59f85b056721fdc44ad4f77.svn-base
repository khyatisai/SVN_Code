package com.unfpa.appsistenciamaternaunfpa.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.unfpa.appsistenciamaternaunfpa.database.dao.country_office.CountryListDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.AppointmentReminderDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.MedicineEventDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.MedicineReminderDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_health.PeriodTrackerDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge.*
import com.unfpa.appsistenciamaternaunfpa.database.dao.myservicedao.MyServiceDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.myservicedao.ServiceCenterDetailDAO
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.*
import com.unfpa.appsistenciamaternaunfpa.database.dao.myvoice.MyVoiceDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.notification.NotificationDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.notification.NotificationDAO2
import com.unfpa.appsistenciamaternaunfpa.database.dao.personal.CountryOfficeDAO
import com.unfpa.appsistenciamaternaunfpa.database.dao.personal.PersonalDetailsDAO
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryList
import com.unfpa.appsistenciamaternaunfpa.database.entity.country_office_listing.CountryOfficeSettingEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.AppointmentReminder
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineEvent
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.MedicineReminder
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_health.PeriodTracker
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.MyServiceEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.myserviceentity.ServiceCenterDetailEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.myvoice.MyVoiceEntity
import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification
import com.unfpa.appsistenciamaternaunfpa.database.entity.notification.Notification2
import com.unfpa.appsistenciamaternaunfpa.database.entity.personaldetails.PersonalDetailsEntity
import com.unfpa.appsistenciamaternaunfpa.utils.Constant


@Database(
    entities = [SRHContent::class,
        ContentDetail::class,
        SRHContentCategory::class,
        ServiceCenterDetailEntity::class,
        MyServiceEntity::class,
        QuizRequest::class,
        QuizResponse::class,
        MyVoiceEntity::class,
        PersonalDetailsEntity::class,
        PeriodTracker::class,
        MedicineReminder::class,
        CountryOfficeSettingEntity::class,
        MedicineEvent::class,
        AppointmentReminder::class,
        Notification::class,
        Notification2::class,
        CountryList::class],

    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
public abstract class MhealthRoomDatabase : RoomDatabase() {

    abstract fun contentMasterDAO(): SRHContentDAO
    abstract fun contentDetailDAO(): ContentDetailDAO
    abstract fun SRHContentCategoryDAO(): SRHContentCategoryDAO
    abstract fun serviceCenterDetailDAO(): ServiceCenterDetailDAO //added by 35251
    abstract fun myServiceDAO(): MyServiceDAO //added by 35251
    abstract fun myVoiceDAO(): MyVoiceDAO //added by 35251
    abstract fun quizRequestDAO(): QuizRequestDAO
    abstract fun personalDetailsDAO(): PersonalDetailsDAO
    abstract fun periodTrackerDAO(): PeriodTrackerDAO
    abstract fun quizResponseDAO(): QuizResponseDAO
    abstract fun medicineReminderDAO(): MedicineReminderDAO
    abstract fun countryOfficeDAO(): CountryOfficeDAO
    abstract fun medicineEventDAO(): MedicineEventDAO
    abstract fun appointmentReminderDAO(): AppointmentReminderDAO
    abstract fun notificationDAO(): NotificationDAO
    abstract fun notificationDAO2(): NotificationDAO2
    abstract fun countryListDAO(): CountryListDAO

    companion object {
        var INSTANCE: MhealthRoomDatabase? = null

        fun getAppDataBase(context: Context): MhealthRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(MhealthRoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MhealthRoomDatabase::class.java,
                        Constant.DB_NAME
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }

}