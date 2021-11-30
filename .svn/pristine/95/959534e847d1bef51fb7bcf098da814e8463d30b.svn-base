package com.unfpa.appsistenciamaternaunfpa.database.dao.my_knowledge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContent


@Dao
public interface SRHContentDAO {
    @Query("SELECT * from content_master")
    fun getAllContent(): List<SRHContent>

    @Query("SELECT * from content_master WHERE nid = :contentId")
    fun getContentById(contentId: String): SRHContent


    /*@Query("SELECT * from content_master WHERE field_thematic_area = :categoryId")
    fun getContentCategoryWise(categoryId: String): List<SRHContent>*/

    @Query("SELECT * from content_master WHERE field_thematic_area_id = :categoryId AND field_hot_srh_content = 'No'")
    fun getContentCategoryWise(categoryId: String): List<SRHContent>

    @Query("SELECT * from content_master WHERE field_hot_srh_content = 'Yes'")
    fun getContentByHotTopics(): List<SRHContent>

    /*@Query("SELECT * from content_master WHERE field_thematic_area = :categoryId AND (title LIKE '%' || :strSearch || '%' OR  field_short_description LIKE '%' || :strSearch || '%')")
    fun getContentListSearch(categoryId: String, strSearch: String): List<SRHContent>*/

    @Query("SELECT * from content_master WHERE field_thematic_area = :categoryName AND (title LIKE '%' || :strSearch || '%' OR  field_short_description LIKE '%' || :strSearch || '%')")
    fun getContentListSearch(categoryName: String, strSearch: String): List<SRHContent>

    @Query("SELECT * from content_master WHERE  (title LIKE '%' || :strSearch || '%' OR  field_short_description LIKE '%' || :strSearch || '%')")
    fun getContentListSearchAll(strSearch: String): List<SRHContent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(contentMaster: SRHContent)

    @Query("DELETE FROM content_master")
    fun deleteAll()

    @Query("UPDATE content_master SET isFavourite = :isFavourite WHERE nid = :nid")
    fun setFavourite(nid: String, isFavourite: Boolean)

    @Query("SELECT * from content_master WHERE isFavourite = 1")
    fun getFavourite(): List<SRHContent>

    @Query("SELECT * from content_master WHERE isFavourite = 1 AND (title LIKE '%' || :strSearch || '%' OR  field_short_description LIKE '%' || :strSearch || '%')")
    fun getFavouriteSearched(strSearch: String): List<SRHContent>

    @Query("SELECT * from content_master WHERE field_tags LIKE '%'|| :strTag ||'%'")
    fun getContentTagwise(strTag: String): List<SRHContent>

    @Query("SELECT * from content_master WHERE (field_tags LIKE '%'|| :strTag ||'%' ) AND (title LIKE '%' || :strSearch || '%' OR  field_short_description LIKE '%' || :strSearch || '%')")
    fun getContentListSearchTags(strTag: String, strSearch: String): List<SRHContent>
}