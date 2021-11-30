package com.unfpa.appsistenciamaternaunfpa.adapters.my_knowledge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unfpa.appsistenciamaternaunfpa.R
import com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge.SRHContentCategory
import com.unfpa.appsistenciamaternaunfpa.utils.AppUtils
import kotlinx.android.synthetic.main.item_srh_category.view.*
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.unfpa.appsistenciamaternaunfpa.fragments.my_knowledge.quiz.QuizListFragment


/**
 * Created by KhyatiShah on 8/16/2019.
 */
class SRHCategoryQuizAdapter(activity: androidx.fragment.app.FragmentActivity) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private lateinit var categoryList: List<SRHContentCategory>
    private var activity: androidx.fragment.app.FragmentActivity = activity

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {


        return CategoryListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_srh_category,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val categoryViewHolder = viewHolder as CategoryListViewHolder
        categoryViewHolder.bindView(categoryList.get(position))
    }

    inner class CategoryListViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindView(category: SRHContentCategory) {
            itemView.txtCategoryName.text = category.name
            itemView.txtCategoryDesc.text = category.description_class
            when (category.name) {
                //setting background image according to category

                activity.getString(R.string.Relationships) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_relationship)
                )

                activity.getString(R.string.SexualAndReproductiveHealth) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_reproductive)
                )

                activity.getString(R.string.SexualityAndSexualBehaviour) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_sexuality)
                )

                activity.getString(R.string.SkillsForHealthAndWellbeing) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_skills)
                )

                activity.getString(R.string.HumanBodyAndDevelopment) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_human_body)
                )

                activity.getString(R.string.UnderstandingGender) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_gender)
                )

                activity.getString(R.string.ValuesRightsCultureAndSexuality) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_value)
                )

                activity.getString(R.string.ViolenceAndStayingSafe) -> itemView.imgBackgroundImgContainer.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.ic_cat_violence)
                )
            }
            itemView.cardCategory.setOnClickListener {
                /* var bundle = Bundle()
                 bundle.putString("categoryId", category.category_id)
                 bundle.putString("categoryName", category.name)
                 var frag = QuizFragment()
                 frag.arguments = bundle
                 AppUtils.addFragment(activity, frag, true, "")*/

                var bundle = Bundle()
                bundle.putString("categoryId", category.category_id)
                bundle.putString("categoryName", category.name)
                var frag = QuizListFragment()
                frag.arguments = bundle
                AppUtils.addFragment(activity, frag, true, "")
            }
        }

    }

    fun setCategoryList(categoryList: List<SRHContentCategory>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}